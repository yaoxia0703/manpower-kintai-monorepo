package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttRequestTest {

    @Test
    void approveMovesPendingRequestToApproved() {
        AttRequest request = AttRequest.pending();

        request.approve();

        assertEquals(ApprovalStatus.APPROVED, request.getStatus());
    }

    @Test
    void rejectMovesPendingRequestToRejected() {
        AttRequest request = AttRequest.pending();

        request.reject();

        assertEquals(ApprovalStatus.REJECTED, request.getStatus());
    }

    @Test
    void cancelOnlyAllowsPendingRequest() {
        AttRequest request = AttRequest.pending();
        request.approve();

        assertThrows(BizException.class, request::cancel);
    }
}
