package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WfApprovalStepTest {

    @Test
    void pendingInitializesApprovalStep() {
        WfApprovalStep step = WfApprovalStep.pending(1L, 2, 10L, 20L);

        assertEquals(1L, step.getApprovalId());
        assertEquals(2, step.getStep());
        assertEquals(10L, step.getApproverId());
        assertEquals(ApprovalStatus.PENDING, step.getStatus());
        assertEquals(20L, step.getCreatedBy());
        assertEquals(20L, step.getUpdatedBy());
        assertNull(step.getComment());
        assertNull(step.getApprovedAt());
    }

    @Test
    void approveMovesPendingStepToApproved() {
        WfApprovalStep step = WfApprovalStep.pending(1L, 2, 10L, 20L);
        LocalDateTime approvedAt = LocalDateTime.of(2026, 7, 10, 9, 0);

        step.approve("ok", approvedAt, 10L);

        assertEquals(ApprovalStatus.APPROVED, step.getStatus());
        assertEquals("ok", step.getComment());
        assertEquals(approvedAt, step.getApprovedAt());
        assertEquals(10L, step.getUpdatedBy());
    }

    @Test
    void rejectMovesPendingStepToRejected() {
        WfApprovalStep step = WfApprovalStep.pending(1L, 2, 10L, 20L);
        LocalDateTime rejectedAt = LocalDateTime.of(2026, 7, 10, 9, 0);

        step.reject("no", rejectedAt, 10L);

        assertEquals(ApprovalStatus.REJECTED, step.getStatus());
        assertEquals("no", step.getComment());
        assertEquals(rejectedAt, step.getApprovedAt());
        assertEquals(10L, step.getUpdatedBy());
    }

    @Test
    void terminalStepCannotChangeAgain() {
        WfApprovalStep step = WfApprovalStep.pending(1L, 2, 10L, 20L);
        step.approve("ok", LocalDateTime.of(2026, 7, 10, 9, 0), 10L);

        assertThrows(BizException.class,
                () -> step.approve("again", LocalDateTime.of(2026, 7, 10, 10, 0), 10L));
        assertThrows(BizException.class,
                () -> step.reject("again", LocalDateTime.of(2026, 7, 10, 10, 0), 10L));
    }
}
