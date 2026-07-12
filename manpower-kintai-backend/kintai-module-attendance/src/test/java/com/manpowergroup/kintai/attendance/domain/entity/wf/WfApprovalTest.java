package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WfApprovalTest {

    @Test
    void startInitializesPendingApprovalAtFirstStep() {
        WfApproval approval = WfApproval.start(1L, "PAID_LEAVE", 2L, 10L, 3, 2L);

        assertEquals(1L, approval.getRequestId());
        assertEquals("PAID_LEAVE", approval.getRequestType());
        assertEquals(2L, approval.getApplicantId());
        assertEquals(10L, approval.getCompanyId());
        assertEquals(1, approval.getCurrentStep());
        assertEquals(3, approval.getTotalSteps());
        assertEquals(ApprovalStatus.PENDING, approval.getStatus());
        assertEquals(0, approval.getEscalated());
        assertEquals(2L, approval.getCreatedBy());
        assertEquals(2L, approval.getUpdatedBy());
        assertNull(approval.getCompletedAt());
    }

    @Test
    void approveCurrentStepMovesToNextStepWhenMoreStepsRemain() {
        WfApproval approval = WfApproval.start(1L, "PAID_LEAVE", 2L, 10L, 3, 2L);

        approval.approveCurrentStep(LocalDateTime.of(2026, 7, 10, 9, 0), 20L);

        assertEquals(2, approval.getCurrentStep());
        assertEquals(ApprovalStatus.PENDING, approval.getStatus());
        assertNull(approval.getCompletedAt());
        assertEquals(20L, approval.getUpdatedBy());
    }

    @Test
    void approveCurrentStepCompletesApprovalAtFinalStep() {
        LocalDateTime completedAt = LocalDateTime.of(2026, 7, 10, 9, 0);
        WfApproval approval = WfApproval.start(1L, "PAID_LEAVE", 2L, 10L, 1, 2L);

        approval.approveCurrentStep(completedAt, 20L);

        assertEquals(1, approval.getCurrentStep());
        assertEquals(ApprovalStatus.APPROVED, approval.getStatus());
        assertEquals(completedAt, approval.getCompletedAt());
        assertEquals(20L, approval.getUpdatedBy());
    }

    @Test
    void rejectCompletesApprovalAsRejected() {
        LocalDateTime rejectedAt = LocalDateTime.of(2026, 7, 10, 9, 0);
        WfApproval approval = WfApproval.start(1L, "PAID_LEAVE", 2L, 10L, 2, 2L);

        approval.reject(rejectedAt, 20L);

        assertEquals(ApprovalStatus.REJECTED, approval.getStatus());
        assertEquals(rejectedAt, approval.getCompletedAt());
        assertEquals(20L, approval.getUpdatedBy());
    }

    @Test
    void terminalApprovalCannotChangeAgain() {
        WfApproval approval = WfApproval.start(1L, "PAID_LEAVE", 2L, 10L, 1, 2L);
        approval.approveCurrentStep(LocalDateTime.of(2026, 7, 10, 9, 0), 20L);

        assertThrows(BizException.class,
                () -> approval.approveCurrentStep(LocalDateTime.of(2026, 7, 10, 10, 0), 20L));
        assertThrows(BizException.class,
                () -> approval.reject(LocalDateTime.of(2026, 7, 10, 10, 0), 20L));
        assertThrows(BizException.class,
                () -> approval.escalateTo(30L, LocalDateTime.of(2026, 7, 10, 10, 0), 20L));
    }

    @Test
    void escalateRecordsEscalationTargetForPendingApproval() {
        LocalDateTime escalatedAt = LocalDateTime.of(2026, 7, 10, 9, 0);
        WfApproval approval = WfApproval.start(1L, "PAID_LEAVE", 2L, 10L, 2, 2L);

        approval.escalateTo(30L, escalatedAt, 20L);

        assertEquals(ApprovalStatus.PENDING, approval.getStatus());
        assertEquals(1, approval.getEscalated());
        assertEquals(30L, approval.getEscalatedTo());
        assertEquals(escalatedAt, approval.getEscalatedAt());
        assertEquals(20L, approval.getUpdatedBy());
    }
}
