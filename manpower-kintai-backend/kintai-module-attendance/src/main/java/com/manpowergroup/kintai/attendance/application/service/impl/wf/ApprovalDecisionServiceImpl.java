package com.manpowergroup.kintai.attendance.application.service.impl.wf;

import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalDecisionService;
import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalDelegateValidator;
import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalNotificationPort;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApproval;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalStep;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRequestRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalStepRepository;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApprovalDecisionServiceImpl implements ApprovalDecisionService {

    private final WfApprovalRepository approvalRepository;
    private final WfApprovalStepRepository stepRepository;
    private final AttRequestRepository requestRepository;
    private final ApprovalDelegateValidator delegateValidator;
    private final ApprovalNotificationPort notificationPort;

    @Override
    @Transactional
    public void approve(Long approvalId, Long approverId, String comment) {
        DecisionContext context = loadContext(approvalId, approverId);
        LocalDateTime decidedAt = LocalDateTime.now();

        context.step().approve(comment, decidedAt, approverId);
        context.approval().approveCurrentStep(decidedAt, approverId);
        stepRepository.update(context.step());
        approvalRepository.update(context.approval());

        if (context.approval().getStatus() == ApprovalStatus.APPROVED) {
            context.request().approve(approverId);
            requestRepository.update(context.request());
            notificationPort.requestApproved(
                    context.approval().getCompanyId(), context.approval().getApplicantId(),
                    context.approval().getRequestType(), context.approval().getRequestId());
        } else {
            WfApprovalStep nextStep = stepRepository
                    .findByApprovalAndStep(approvalId, context.approval().getCurrentStep())
                    .orElseThrow(() -> notFound("next approval step not found"));
            notificationPort.requestSubmitted(
                    context.approval().getCompanyId(), nextStep.getApproverId(),
                    context.approval().getRequestType(), context.approval().getRequestId());
        }
    }

    @Override
    @Transactional
    public void reject(Long approvalId, Long approverId, String comment) {
        DecisionContext context = loadContext(approvalId, approverId);
        LocalDateTime decidedAt = LocalDateTime.now();

        context.step().reject(comment, decidedAt, approverId);
        context.approval().reject(decidedAt, approverId);
        context.request().reject(approverId);
        stepRepository.update(context.step());
        stepRepository.listPendingByApproval(approvalId).stream()
                .filter(step -> step.getStatus() == ApprovalStatus.PENDING)
                .forEach(step -> {
                    step.cancel(decidedAt, approverId);
                    stepRepository.update(step);
                });
        approvalRepository.update(context.approval());
        requestRepository.update(context.request());
        notificationPort.requestRejected(
                context.approval().getCompanyId(), context.approval().getApplicantId(),
                context.approval().getRequestType(), context.approval().getRequestId());
    }

    @Override
    @Transactional
    public void delegate(Long approvalId, Long approverId, Long targetApproverId) {
        WfApproval approval = approvalRepository.findByIdForUpdate(approvalId)
                .orElseThrow(() -> notFound("approval not found"));
        WfApprovalStep step = stepRepository
                .findByApprovalAndStep(approvalId, approval.getCurrentStep())
                .orElseThrow(() -> notFound("current approval step not found"));
        requireAuthorizedApprover(approval, step, approverId);
        if (Objects.equals(targetApproverId, approval.getApplicantId())) {
            throw BizException.withDetail(ErrorCode.CONFLICT,
                    "attendance request cannot be delegated to applicant");
        }
        if (Objects.equals(targetApproverId, approverId)) {
            throw BizException.withDetail(ErrorCode.CONFLICT,
                    "attendance request cannot be delegated to current approver");
        }
        delegateValidator.validateTarget(targetApproverId, approval.getCompanyId());
        approval.escalateTo(targetApproverId, LocalDateTime.now(), approverId);
        approvalRepository.update(approval);
        notificationPort.approvalDelegated(
                approval.getCompanyId(), targetApproverId,
                approval.getRequestType(), approval.getRequestId());
    }

    private DecisionContext loadContext(Long approvalId, Long approverId) {
        WfApproval approval = approvalRepository.findByIdForUpdate(approvalId)
                .orElseThrow(() -> notFound("approval not found"));
        WfApprovalStep step = stepRepository.findByApprovalAndStep(approvalId, approval.getCurrentStep())
                .orElseThrow(() -> notFound("current approval step not found"));
        AttRequest request = requestRepository.findById(approval.getRequestId())
                .orElseThrow(() -> notFound("attendance request not found"));
        requireAuthorizedApprover(approval, step, approverId);
        return new DecisionContext(approval, step, request);
    }

    private void requireAuthorizedApprover(
            WfApproval approval, WfApprovalStep step, Long approverId) {
        boolean authorized = Objects.equals(approval.getEscalated(), 1)
                ? Objects.equals(approval.getEscalatedTo(), approverId)
                : Objects.equals(step.getApproverId(), approverId);
        if (!authorized) {
            throw BizException.withDetail(ErrorCode.FORBIDDEN,
                    "employee is not assigned to the current approval step");
        }
    }

    private BizException notFound(String detail) {
        return BizException.withDetail(ErrorCode.NOT_FOUND, detail);
    }

    private record DecisionContext(WfApproval approval, WfApprovalStep step, AttRequest request) {
    }
}
