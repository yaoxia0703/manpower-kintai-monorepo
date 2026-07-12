package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.manpowergroup.kintai.attendance.application.command.att.AttRequestCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.att.AttRequestUpdateCommand;
import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalRouteResolver;
import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalNotificationPort;
import com.manpowergroup.kintai.attendance.application.service.att.AttRequestService;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApproval;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalStep;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRequestRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalStepRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRuleRepository;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AttRequestServiceImpl implements AttRequestService {

    private final AttRequestRepository repository;
    private final WfApprovalRepository approvalRepository;
    private final WfApprovalStepRepository stepRepository;
    private final WfApprovalRuleRepository ruleRepository;
    private final ApprovalRouteResolver routeResolver;
    private final ApprovalNotificationPort notificationPort;

    @Override
    @Transactional
    public AttRequest create(AttRequestCreateCommand command) {
        AttRequest request = AttRequest.create(
                command.employeeId(),
                command.companyId(),
                command.requestType(),
                command.startDate(),
                command.endDate(),
                command.startTime(),
                command.endTime(),
                command.days(),
                command.minutes(),
                command.reason());
        repository.save(request);

        BigDecimal amount = requestAmount(command);
        WfApprovalRule rule = ruleRepository
                .findApplicable(command.companyId(), command.requestType(), amount)
                .orElse(null);
        List<Long> approvers = routeResolver.resolveApprovers(
                command.employeeId(), command.companyId(), rule);
        if (approvers == null || approvers.isEmpty() || approvers.stream().anyMatch(id -> id == null)) {
            throw BizException.withDetail(ErrorCode.CONFLICT,
                    "attendance request has no valid approval route");
        }
        WfApproval approval = WfApproval.start(
                request.getId(),
                request.getRequestType(),
                request.getEmployeeId(),
                request.getCompanyId(),
                approvers.size(),
                command.employeeId());
        approvalRepository.save(approval);
        for (int index = 0; index < approvers.size(); index++) {
            stepRepository.save(WfApprovalStep.pending(
                    approval.getId(), index + 1, approvers.get(index), command.employeeId()));
        }
        notificationPort.requestSubmitted(
                request.getCompanyId(), approvers.get(0), request.getRequestType(), request.getId());
        return request;
    }

    @Override
    @Transactional
    public AttRequest update(AttRequestUpdateCommand command) {
        AttRequest request = requireOwned(command.requestId(), command.employeeId());
        request.updateDetails(
                command.requestType(),
                command.startDate(),
                command.endDate(),
                command.startTime(),
                command.endTime(),
                command.days(),
                command.minutes(),
                command.reason(),
                command.employeeId());
        repository.update(request);
        return request;
    }

    @Override
    @Transactional
    public void cancel(Long employeeId, Long requestId) {
        AttRequest request = requireOwned(requestId, employeeId);
        approvalRepository.findByRequestIdForUpdate(requestId).ifPresent(approval -> {
            List<WfApprovalStep> pendingSteps = stepRepository.listPendingByApproval(approval.getId());
            if (pendingSteps.isEmpty()) {
                throw BizException.withDetail(ErrorCode.NOT_FOUND,
                        "pending approval steps not found");
            }
            Long currentApproverId = currentApprover(approval, pendingSteps);
            LocalDateTime cancelledAt = LocalDateTime.now();
            approval.cancel(cancelledAt, employeeId);
            approvalRepository.update(approval);
            pendingSteps.forEach(step -> {
                step.cancel(cancelledAt, employeeId);
                stepRepository.update(step);
            });
            notificationPort.requestCancelled(
                    request.getCompanyId(), currentApproverId,
                    request.getRequestType(), requestId);
        });
        request.cancel(employeeId);
        repository.update(request);
    }

    @Override
    public List<AttRequest> listByEmployee(Long employeeId) {
        return repository.listByEmployee(employeeId);
    }

    private AttRequest requireOwned(Long requestId, Long employeeId) {
        return repository.findByIdAndEmployee(requestId, employeeId)
                .orElseThrow(() -> BizException.withDetail(
                        ErrorCode.NOT_FOUND, "attendance request not found"));
    }

    private BigDecimal requestAmount(AttRequestCreateCommand command) {
        if (command.days() != null) {
            return command.days();
        }
        return command.minutes() == null ? null : BigDecimal.valueOf(command.minutes());
    }

    private Long currentApprover(WfApproval approval, List<WfApprovalStep> pendingSteps) {
        if (Integer.valueOf(1).equals(approval.getEscalated())) {
            return approval.getEscalatedTo();
        }
        return pendingSteps.stream()
                .filter(step -> step.getStep().equals(approval.getCurrentStep()))
                .map(WfApprovalStep::getApproverId)
                .findFirst()
                .orElseThrow(() -> BizException.withDetail(
                        ErrorCode.NOT_FOUND, "current approval step not found"));
    }
}
