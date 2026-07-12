package com.manpowergroup.kintai.attendance.application.service.impl.wf;

import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApproval;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalStep;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRequestRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRepository;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalStepRepository;
import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalDelegateValidator;
import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalNotificationPort;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApprovalDecisionServiceImplTest {

    @Test
    void approveFinalStepCompletesApprovalAndAttendanceRequest() {
        Fixture fixture = fixture(1);

        fixture.service.approve(7L, 20L, "approved");

        assertEquals(ApprovalStatus.APPROVED, fixture.step.getStatus());
        assertEquals(ApprovalStatus.APPROVED, fixture.approval.getStatus());
        assertEquals(ApprovalStatus.APPROVED, fixture.request.getStatus());
        assertEquals(20L, fixture.request.getUpdatedBy());
        verify(fixture.stepRepository).update(fixture.step);
        verify(fixture.approvalRepository).update(fixture.approval);
        verify(fixture.requestRepository).update(fixture.request);
        verify(fixture.notificationPort).requestApproved(10L, 1L, "PAID_LEAVE", 99L);
    }

    @Test
    void approveIntermediateStepKeepsAttendanceRequestPending() {
        Fixture fixture = fixture(2);

        fixture.service.approve(7L, 20L, "approved");

        assertEquals(ApprovalStatus.APPROVED, fixture.step.getStatus());
        assertEquals(ApprovalStatus.PENDING, fixture.approval.getStatus());
        assertEquals(2, fixture.approval.getCurrentStep());
        assertEquals(ApprovalStatus.PENDING, fixture.request.getStatus());
        verify(fixture.requestRepository, never()).update(fixture.request);
        verify(fixture.notificationPort).requestSubmitted(10L, 30L, "PAID_LEAVE", 99L);
    }

    @Test
    void rejectStepRejectsApprovalAndAttendanceRequest() {
        Fixture fixture = fixture(2);

        fixture.service.reject(7L, 20L, "rejected");

        assertEquals(ApprovalStatus.REJECTED, fixture.step.getStatus());
        assertEquals(ApprovalStatus.CANCELLED, fixture.nextStep.getStatus());
        assertEquals(ApprovalStatus.REJECTED, fixture.approval.getStatus());
        assertEquals(ApprovalStatus.REJECTED, fixture.request.getStatus());
        assertEquals(20L, fixture.request.getUpdatedBy());
        verify(fixture.requestRepository).update(fixture.request);
        verify(fixture.notificationPort).requestRejected(10L, 1L, "PAID_LEAVE", 99L);
    }

    @Test
    void decisionByEmployeeNotAssignedToCurrentStepIsRejectedWithoutUpdates() {
        Fixture fixture = fixture(1);

        assertThrows(BizException.class,
                () -> fixture.service.approve(7L, 30L, "not my approval"));

        assertEquals(ApprovalStatus.PENDING, fixture.step.getStatus());
        assertEquals(ApprovalStatus.PENDING, fixture.approval.getStatus());
        assertEquals(ApprovalStatus.PENDING, fixture.request.getStatus());
        verify(fixture.stepRepository, never()).update(Mockito.any());
        verify(fixture.approvalRepository, never()).update(Mockito.any());
        verify(fixture.requestRepository, never()).update(Mockito.any());
    }

    @Test
    void currentApproverCanDelegateToValidatedEmployee() {
        Fixture fixture = fixture(1);

        fixture.service.delegate(7L, 20L, 30L);

        assertEquals(1, fixture.approval.getEscalated());
        assertEquals(30L, fixture.approval.getEscalatedTo());
        verify(fixture.delegateValidator).validateTarget(30L, 10L);
        verify(fixture.approvalRepository).update(fixture.approval);
        verify(fixture.notificationPort).approvalDelegated(10L, 30L, "PAID_LEAVE", 99L);
    }

    @Test
    void delegatedApprovalCanOnlyBeDecidedByNewApprover() {
        Fixture fixture = fixture(1);
        fixture.approval.escalateTo(
                30L, LocalDateTime.of(2026, 7, 11, 9, 0), 20L);

        assertThrows(BizException.class,
                () -> fixture.service.approve(7L, 20L, "original approver"));

        fixture.service.approve(7L, 30L, "delegated approver");
        assertEquals(ApprovalStatus.APPROVED, fixture.approval.getStatus());
    }

    @Test
    void delegationIsClearedAfterCurrentStepIsApproved() {
        Fixture fixture = fixture(2);
        fixture.service.delegate(7L, 20L, 40L);

        fixture.service.approve(7L, 40L, "approved delegated step");

        assertEquals(2, fixture.approval.getCurrentStep());
        assertEquals(0, fixture.approval.getEscalated());
        assertEquals(null, fixture.approval.getEscalatedTo());
    }

    private Fixture fixture(int totalSteps) {
        WfApprovalRepository approvalRepository = Mockito.mock(WfApprovalRepository.class);
        WfApprovalStepRepository stepRepository = Mockito.mock(WfApprovalStepRepository.class);
        AttRequestRepository requestRepository = Mockito.mock(AttRequestRepository.class);
        ApprovalDelegateValidator delegateValidator = Mockito.mock(ApprovalDelegateValidator.class);
        ApprovalNotificationPort notificationPort = Mockito.mock(ApprovalNotificationPort.class);
        ApprovalDecisionServiceImpl service = new ApprovalDecisionServiceImpl(
                approvalRepository, stepRepository, requestRepository, delegateValidator,
                notificationPort);

        WfApproval approval = WfApproval.start(99L, "PAID_LEAVE", 1L, 10L, totalSteps, 1L)
                .setId(7L);
        WfApprovalStep step = WfApprovalStep.pending(7L, 1, 20L, 1L);
        WfApprovalStep nextStep = totalSteps > 1
                ? WfApprovalStep.pending(7L, 2, 30L, 1L)
                : null;
        AttRequest request = AttRequest.create(
                1L, 10L, "PAID_LEAVE",
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 10),
                null, null, BigDecimal.ONE, null, "leave");

        when(approvalRepository.findByIdForUpdate(7L)).thenReturn(Optional.of(approval));
        when(stepRepository.findByApprovalAndStep(7L, 1)).thenReturn(Optional.of(step));
        if (nextStep != null) {
            when(stepRepository.findByApprovalAndStep(7L, 2)).thenReturn(Optional.of(nextStep));
        }
        when(stepRepository.listPendingByApproval(7L)).thenReturn(
                nextStep == null ? List.of(step) : List.of(step, nextStep));
        when(requestRepository.findById(99L)).thenReturn(Optional.of(request));
        return new Fixture(service, approvalRepository, stepRepository, requestRepository,
                delegateValidator,
                notificationPort,
                approval, step, nextStep, request);
    }

    private record Fixture(
            ApprovalDecisionServiceImpl service,
            WfApprovalRepository approvalRepository,
            WfApprovalStepRepository stepRepository,
            AttRequestRepository requestRepository,
            ApprovalDelegateValidator delegateValidator,
            ApprovalNotificationPort notificationPort,
            WfApproval approval,
            WfApprovalStep step,
            WfApprovalStep nextStep,
            AttRequest request
    ) {
    }
}
