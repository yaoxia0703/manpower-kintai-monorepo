package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttRequestTest {

    @Test
    void createInitializesBusinessFieldsAndPendingStatus() {
        AttRequest request = AttRequest.create(
                1L,
                10L,
                "PAID_LEAVE",
                LocalDate.of(2026, 6, 6),
                LocalDate.of(2026, 6, 7),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                BigDecimal.valueOf(2),
                960,
                "年次有給休暇"
        );

        assertEquals(1L, request.getEmployeeId());
        assertEquals(10L, request.getCompanyId());
        assertEquals("PAID_LEAVE", request.getRequestType());
        assertEquals(LocalDate.of(2026, 6, 6), request.getStartDate());
        assertEquals(LocalDate.of(2026, 6, 7), request.getEndDate());
        assertEquals(LocalTime.of(9, 0), request.getStartTime());
        assertEquals(LocalTime.of(18, 0), request.getEndTime());
        assertEquals(BigDecimal.valueOf(2), request.getDays());
        assertEquals(960, request.getMinutes());
        assertEquals("年次有給休暇", request.getReason());
        assertEquals(ApprovalStatus.PENDING, request.getStatus());
    }

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

        request.cancel();

        assertEquals(ApprovalStatus.CANCELLED, request.getStatus());
    }

    @Test
    void cancelRecordsActor() {
        AttRequest request = createRequest();

        request.cancel(2L);

        assertEquals(ApprovalStatus.CANCELLED, request.getStatus());
        assertEquals(2L, request.getUpdatedBy());
    }

    @Test
    void approvedRequestCannotChangeStatusAgain() {
        AttRequest request = AttRequest.pending();
        request.approve();

        assertThrows(BizException.class, request::approve);
        assertThrows(BizException.class, request::reject);
        assertThrows(BizException.class, request::cancel);
    }

    @Test
    void rejectedRequestCannotChangeStatusAgain() {
        AttRequest request = AttRequest.pending();
        request.reject();

        assertThrows(BizException.class, request::approve);
        assertThrows(BizException.class, request::reject);
        assertThrows(BizException.class, request::cancel);
    }

    @Test
    void cancelledRequestCannotChangeStatusAgain() {
        AttRequest request = AttRequest.pending();
        request.cancel();

        assertThrows(BizException.class, request::approve);
        assertThrows(BizException.class, request::reject);
        assertThrows(BizException.class, request::cancel);
    }

    @Test
    void createRejectsEndDateBeforeStartDate() {
        assertThrows(BizException.class, () -> AttRequest.create(
                1L,
                10L,
                "PAID_LEAVE",
                LocalDate.of(2026, 7, 2),
                LocalDate.of(2026, 7, 1),
                null,
                null,
                BigDecimal.ONE,
                null,
                "leave"));
    }

    @Test
    void pendingRequestCanUpdateDetails() {
        AttRequest request = createRequest();

        request.updateDetails(
                "OVERTIME",
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 10),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                null,
                120,
                "overtime",
                2L);

        assertEquals("OVERTIME", request.getRequestType());
        assertEquals(120, request.getMinutes());
        assertEquals("overtime", request.getReason());
        assertEquals(2L, request.getUpdatedBy());
    }

    @Test
    void approvedRequestCannotUpdateDetails() {
        AttRequest request = createRequest();
        request.approve();

        assertThrows(BizException.class, () -> request.updateDetails(
                "OVERTIME",
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 10),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                null,
                120,
                "overtime",
                2L));
    }

    private AttRequest createRequest() {
        return AttRequest.create(
                1L,
                10L,
                "PAID_LEAVE",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 1),
                null,
                null,
                BigDecimal.ONE,
                null,
                "leave");
    }
}
