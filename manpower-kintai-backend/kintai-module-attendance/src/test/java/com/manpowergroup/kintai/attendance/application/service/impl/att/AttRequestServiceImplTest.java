package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.attendance.application.command.att.AttRequestCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.att.AttRequestUpdateCommand;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApproval;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalStep;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRequestRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalStepRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRuleRepository;
import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalRouteResolver;
import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalNotificationPort;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class AttRequestServiceImplTest {

    @Test
    void createStartsDirectManagerApprovalInSameUseCase() throws Exception {
        AttRequestRepository repository = Mockito.mock(AttRequestRepository.class);
        WfApprovalRepository approvalRepository = Mockito.mock(WfApprovalRepository.class);
        WfApprovalStepRepository stepRepository = Mockito.mock(WfApprovalStepRepository.class);
        WfApprovalRuleRepository ruleRepository = Mockito.mock(WfApprovalRuleRepository.class);
        ApprovalRouteResolver routeResolver = Mockito.mock(ApprovalRouteResolver.class);
        ApprovalNotificationPort notificationPort = Mockito.mock(ApprovalNotificationPort.class);
        AttRequestServiceImpl service = new AttRequestServiceImpl(
                repository, approvalRepository, stepRepository, ruleRepository, routeResolver,
                notificationPort);
        AttRequestCreateCommand command = new AttRequestCreateCommand(
                1L, 10L, RequestType.PAID_LEAVE,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 10),
                null, null, BigDecimal.ONE, null, "leave");
        when(ruleRepository.findApplicable(10L, RequestType.PAID_LEAVE, BigDecimal.ONE))
                .thenReturn(Optional.empty());
        when(routeResolver.resolveApprovers(1L, 10L, null)).thenReturn(List.of(20L));
        when(repository.save(any(AttRequest.class))).thenAnswer(invocation -> {
            AttRequest request = invocation.getArgument(0);
            var id = AttRequest.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(request, 99L);
            return request;
        });
        when(approvalRepository.save(any(WfApproval.class))).thenAnswer(invocation ->
                invocation.<WfApproval>getArgument(0).setId(7L));

        AttRequest result = service.create(command);

        ArgumentCaptor<WfApproval> approvalCaptor = ArgumentCaptor.forClass(WfApproval.class);
        ArgumentCaptor<WfApprovalStep> stepCaptor = ArgumentCaptor.forClass(WfApprovalStep.class);
        verify(approvalRepository).save(approvalCaptor.capture());
        verify(stepRepository).save(stepCaptor.capture());
        WfApproval approval = approvalCaptor.getValue();
        WfApprovalStep step = stepCaptor.getValue();

        assertEquals(1L, result.getEmployeeId());
        assertEquals(ApprovalStatus.PENDING, result.getStatus());
        assertEquals(99L, approval.getRequestId());
        assertEquals(1, approval.getTotalSteps());
        assertEquals(7L, step.getApprovalId());
        assertEquals(20L, step.getApproverId());
        assertEquals(ApprovalStatus.PENDING, step.getStatus());
        verify(notificationPort).requestSubmitted(10L, 20L, RequestType.PAID_LEAVE, 99L);
    }

    @Test
    void createBuildsOrderedStepsFromApplicableApprovalRule() throws Exception {
        AttRequestRepository repository = Mockito.mock(AttRequestRepository.class);
        WfApprovalRepository approvalRepository = Mockito.mock(WfApprovalRepository.class);
        WfApprovalStepRepository stepRepository = Mockito.mock(WfApprovalStepRepository.class);
        WfApprovalRuleRepository ruleRepository = Mockito.mock(WfApprovalRuleRepository.class);
        ApprovalRouteResolver routeResolver = Mockito.mock(ApprovalRouteResolver.class);
        ApprovalNotificationPort notificationPort = Mockito.mock(ApprovalNotificationPort.class);
        AttRequestServiceImpl service = new AttRequestServiceImpl(
                repository, approvalRepository, stepRepository, ruleRepository, routeResolver,
                notificationPort);
        AttRequestCreateCommand command = new AttRequestCreateCommand(
                1L, 10L, RequestType.PAID_LEAVE,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12),
                null, null, BigDecimal.valueOf(99), null, "leave");
        WfApprovalRule rule = WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.REACH_DEPARTMENT,
                null, "HR", null, 1, Status.ENABLED);
        when(ruleRepository.findApplicable(10L, RequestType.PAID_LEAVE, BigDecimal.ONE))
                .thenReturn(Optional.of(rule));
        when(routeResolver.resolveApprovers(1L, 10L, rule)).thenReturn(List.of(20L, 30L));
        when(routeResolver.resolveApprovers(1L, 10L, null)).thenReturn(List.of(20L, 30L));
        when(repository.save(any(AttRequest.class))).thenAnswer(invocation -> {
            AttRequest request = invocation.getArgument(0);
            var id = AttRequest.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(request, 99L);
            return request;
        });
        when(approvalRepository.save(any(WfApproval.class))).thenAnswer(invocation ->
                invocation.<WfApproval>getArgument(0).setId(7L));

        service.create(command);

        ArgumentCaptor<WfApproval> approvalCaptor = ArgumentCaptor.forClass(WfApproval.class);
        ArgumentCaptor<WfApprovalStep> stepsCaptor = ArgumentCaptor.forClass(WfApprovalStep.class);
        verify(approvalRepository).save(approvalCaptor.capture());
        verify(stepRepository, Mockito.times(2)).save(stepsCaptor.capture());
        verify(ruleRepository).findApplicable(10L, RequestType.PAID_LEAVE, BigDecimal.ONE);
        assertEquals(2, approvalCaptor.getValue().getTotalSteps());
        assertEquals(List.of(1, 2), stepsCaptor.getAllValues().stream()
                .map(WfApprovalStep::getStep)
                .toList());
        assertEquals(List.of(20L, 30L), stepsCaptor.getAllValues().stream()
                .map(WfApprovalStep::getApproverId)
                .toList());
        verify(notificationPort).requestSubmitted(10L, 20L, RequestType.PAID_LEAVE, 99L);
    }

    @Test
    void updateLoadsOwnedRequestAndUsesDomainBehavior() {
        AttRequestRepository repository = Mockito.mock(AttRequestRepository.class);
        AttRequestServiceImpl service = service(repository);
        AttRequest existing = request();
        when(repository.findByIdAndEmployee(99L, 1L)).thenReturn(Optional.of(existing));
        AttRequestUpdateCommand command = new AttRequestUpdateCommand(
                99L, 1L, RequestType.OVERTIME,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 10),
                LocalTime.of(18, 0), LocalTime.of(20, 0),
                null, 120, "overtime");

        AttRequest result = service.update(command);

        assertEquals(RequestType.OVERTIME, result.getRequestType());
        assertEquals(120, result.getMinutes());
        verify(repository).update(existing);
    }

    @Test
    void cancelRejectsRequestNotOwnedByEmployee() {
        AttRequestRepository repository = Mockito.mock(AttRequestRepository.class);
        AttRequestServiceImpl service = service(repository);
        when(repository.findByIdAndEmployee(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(BizException.class, () -> service.cancel(1L, 99L));

        verify(repository, never()).update(Mockito.any(AttRequest.class));
    }

    @Test
    void cancelUsesDomainTransitionAndPersists() {
        AttRequestRepository repository = Mockito.mock(AttRequestRepository.class);
        WfApprovalRepository approvalRepository = Mockito.mock(WfApprovalRepository.class);
        WfApprovalStepRepository stepRepository = Mockito.mock(WfApprovalStepRepository.class);
        ApprovalNotificationPort notificationPort = Mockito.mock(ApprovalNotificationPort.class);
        AttRequestServiceImpl service = new AttRequestServiceImpl(
                repository, approvalRepository, stepRepository,
                Mockito.mock(WfApprovalRuleRepository.class),
                Mockito.mock(ApprovalRouteResolver.class), notificationPort);
        AttRequest existing = request();
        WfApproval approval = WfApproval.start(99L, RequestType.PAID_LEAVE, 1L, 10L, 1, 1L)
                .setId(7L);
        WfApprovalStep step = WfApprovalStep.pending(7L, 1, 20L, 1L);
        when(repository.findByIdAndEmployee(99L, 1L)).thenReturn(Optional.of(existing));
        when(approvalRepository.findByRequestIdForUpdate(99L)).thenReturn(Optional.of(approval));
        when(stepRepository.listPendingByApproval(7L)).thenReturn(List.of(step));

        service.cancel(1L, 99L);

        assertEquals(ApprovalStatus.CANCELLED, existing.getStatus());
        assertEquals(ApprovalStatus.CANCELLED, approval.getStatus());
        assertEquals(ApprovalStatus.CANCELLED, step.getStatus());
        assertEquals(1L, existing.getUpdatedBy());
        verify(repository).update(existing);
        verify(approvalRepository).update(approval);
        verify(stepRepository).update(step);
        verify(notificationPort).requestCancelled(10L, 20L, RequestType.PAID_LEAVE, 99L);
    }

    @Test
    void listByEmployeeDelegatesToOwnedRequestRepositoryQuery() {
        AttRequestRepository repository = Mockito.mock(AttRequestRepository.class);
        AttRequestServiceImpl service = service(repository);
        List<AttRequest> requests = List.of(request());
        when(repository.listByEmployee(1L)).thenReturn(requests);

        assertEquals(requests, service.listByEmployee(1L));
    }

    private AttRequest request() {
        return AttRequest.create(
                1L, 10L, RequestType.PAID_LEAVE,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 10),
                null, null, BigDecimal.ONE, null, "leave");
    }

    private AttRequestServiceImpl service(AttRequestRepository repository) {
        return new AttRequestServiceImpl(
                repository,
                Mockito.mock(WfApprovalRepository.class),
                Mockito.mock(WfApprovalStepRepository.class),
                Mockito.mock(WfApprovalRuleRepository.class),
                Mockito.mock(ApprovalRouteResolver.class),
                Mockito.mock(ApprovalNotificationPort.class));
    }
}
