package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttRequestTest {

    @Test
    void createInitializesBusinessFieldsAndPendingStatus() {
        AttRequest request = AttRequest.create(
                1L,
                10L,
                RequestType.PAID_LEAVE,
                LocalDate.of(2026, 6, 8),
                LocalDate.of(2026, 6, 9),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                BigDecimal.valueOf(99),
                960,
                "年次有給休暇"
        );

        assertEquals(1L, request.getEmployeeId());
        assertEquals(10L, request.getCompanyId());
        assertEquals(RequestType.PAID_LEAVE, request.getRequestType());
        assertEquals(LocalDate.of(2026, 6, 8), request.getStartDate());
        assertEquals(LocalDate.of(2026, 6, 9), request.getEndDate());
        assertEquals(null, request.getStartTime());
        assertEquals(null, request.getEndTime());
        assertEquals(BigDecimal.valueOf(2), request.getDays());
        assertEquals(null, request.getMinutes());
        assertEquals("年次有給休暇", request.getReason());
        assertEquals(ApprovalStatus.PENDING, request.getStatus());
    }

    @Test
    void createCountsOnlyWeekdaysForWholeDayLeave() {
        AttRequest request = AttRequest.create(
                1L, 10L, RequestType.SUBSTITUTE,
                LocalDate.of(2026, 7, 17), LocalDate.of(2026, 7, 20),
                null, null, BigDecimal.valueOf(99), null, "substitute leave");

        assertEquals(BigDecimal.valueOf(2), request.getDays());
    }

    @Test
    void createCalculatesOvertimeMinutesFromStartAndEndTime() {
        AttRequest request = AttRequest.create(
                1L, 10L, RequestType.OVERTIME,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 10),
                LocalTime.of(18, 0), LocalTime.of(20, 30),
                BigDecimal.valueOf(99), 999, "overtime");

        assertEquals(null, request.getDays());
        assertEquals(150, request.getMinutes());
    }

    @Test
    void createRejectsUnsupportedRequestType() {
        assertThrows(BizException.class, () -> AttRequest.create(
                1L, 10L, RequestType.BUSINESS_TRIP,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 10),
                null, null, BigDecimal.ONE, null, "trip"));
    }

    @Test
    void createRejectsLeaveRangeWithoutWeekdays() {
        assertThrows(BizException.class, () -> AttRequest.create(
                1L, 10L, RequestType.PAID_LEAVE,
                LocalDate.of(2026, 7, 18), LocalDate.of(2026, 7, 19),
                null, null, BigDecimal.valueOf(2), null, "weekend"));
    }

    @Test
    void createRejectsOvertimeWithoutPositiveTimeRange() {
        assertThrows(BizException.class, () -> AttRequest.create(
                1L, 10L, RequestType.OVERTIME,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 10),
                LocalTime.of(20, 0), LocalTime.of(18, 0),
                null, 120, "overtime"));
    }

    @Test
    void createRejectsOvertimeSpanningMultipleDates() {
        assertThrows(BizException.class, () -> AttRequest.create(
                1L, 10L, RequestType.OVERTIME,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 11),
                LocalTime.of(18, 0), LocalTime.of(20, 0),
                null, 120, "overtime"));
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
                RequestType.PAID_LEAVE,
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
                RequestType.OVERTIME,
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 10),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                BigDecimal.valueOf(99),
                999,
                "overtime",
                2L);

        assertEquals(RequestType.OVERTIME, request.getRequestType());
        assertEquals(null, request.getDays());
        assertEquals(120, request.getMinutes());
        assertEquals("overtime", request.getReason());
        assertEquals(2L, request.getUpdatedBy());
    }

    @Test
    void approvedRequestCannotUpdateDetails() {
        AttRequest request = createRequest();
        request.approve();

        assertThrows(BizException.class, () -> request.updateDetails(
                RequestType.OVERTIME,
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 10),
                LocalTime.of(18, 0),
                LocalTime.of(20, 0),
                null,
                120,
                "overtime",
                2L));
    }

    @Test
    void pendingAndApprovedLeaveRequestsLockCoveredTimesheetDates() {
        AttRequest request = createRequest();

        assertTrue(request.locksTimesheetOn(LocalDate.of(2026, 7, 1)));
        assertFalse(request.locksTimesheetOn(LocalDate.of(2026, 7, 2)));

        request.approve();

        assertTrue(request.locksTimesheetOn(LocalDate.of(2026, 7, 1)));
    }

    @Test
    void rejectedLeaveAndOvertimeRequestsDoNotLockTimesheet() {
        AttRequest rejectedLeave = createRequest();
        rejectedLeave.reject();
        AttRequest overtime = AttRequest.create(
                1L, 10L, RequestType.OVERTIME,
                LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 1),
                LocalTime.of(18, 0), LocalTime.of(20, 0),
                null, 120, "overtime");

        assertFalse(rejectedLeave.locksTimesheetOn(LocalDate.of(2026, 7, 1)));
        assertFalse(overtime.locksTimesheetOn(LocalDate.of(2026, 7, 1)));
    }

    private AttRequest createRequest() {
        return AttRequest.create(
                1L,
                10L,
                RequestType.PAID_LEAVE,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 1),
                null,
                null,
                BigDecimal.ONE,
                null,
                "leave");
    }
}
