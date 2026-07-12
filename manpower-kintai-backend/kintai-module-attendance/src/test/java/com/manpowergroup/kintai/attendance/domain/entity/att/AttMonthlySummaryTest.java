package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.manpowergroup.kintai.attendance.domain.enums.MonthlySummaryStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttMonthlySummaryTest {

    @Test
    void createOpenInitializesMonthlyTotalsAndOpenStatus() {
        AttMonthlySummary summary = AttMonthlySummary.createOpen(
                1L,
                10L,
                "2026-07",
                20,
                1,
                BigDecimal.valueOf(2.5),
                9600,
                300,
                2,
                1);

        assertEquals(1L, summary.getEmployeeId());
        assertEquals(10L, summary.getCompanyId());
        assertEquals("2026-07", summary.getSummaryMonth());
        assertEquals(20, summary.getWorkDays());
        assertEquals(1, summary.getAbsentDays());
        assertEquals(BigDecimal.valueOf(2.5), summary.getPaidLeaveDays());
        assertEquals(9600, summary.getTotalWorkMinutes());
        assertEquals(300, summary.getTotalOvertimeMinutes());
        assertEquals(2, summary.getLateCount());
        assertEquals(1, summary.getEarlyLeaveCount());
        assertEquals(MonthlySummaryStatus.OPEN, summary.getStatus());
        assertNull(summary.getConfirmedAt());
        assertNull(summary.getConfirmedBy());
    }

    @Test
    void confirmMovesOpenSummaryToConfirmed() {
        AttMonthlySummary summary = AttMonthlySummary.createOpen(
                1L, 10L, "2026-07", 20, 1, BigDecimal.ZERO, 9600, 0, 0, 0);
        LocalDateTime confirmedAt = LocalDateTime.of(2026, 8, 1, 9, 0);

        summary.confirm(99L, confirmedAt);

        assertEquals(MonthlySummaryStatus.CONFIRMED, summary.getStatus());
        assertEquals(99L, summary.getConfirmedBy());
        assertEquals(confirmedAt, summary.getConfirmedAt());
    }

    @Test
    void confirmedSummaryCannotBeConfirmedAgain() {
        AttMonthlySummary summary = AttMonthlySummary.createOpen(
                1L, 10L, "2026-07", 20, 1, BigDecimal.ZERO, 9600, 0, 0, 0);
        summary.confirm(99L, LocalDateTime.of(2026, 8, 1, 9, 0));

        assertThrows(BizException.class, () -> summary.confirm(99L, LocalDateTime.of(2026, 8, 1, 10, 0)));
    }

    @Test
    void reopenMovesConfirmedSummaryBackToOpenAndClearsConfirmation() {
        AttMonthlySummary summary = AttMonthlySummary.createOpen(
                1L, 10L, "2026-07", 20, 1, BigDecimal.ZERO, 9600, 0, 0, 0);
        summary.confirm(99L, LocalDateTime.of(2026, 8, 1, 9, 0));

        summary.reopen();

        assertEquals(MonthlySummaryStatus.OPEN, summary.getStatus());
        assertNull(summary.getConfirmedBy());
        assertNull(summary.getConfirmedAt());
    }

    @Test
    void openSummaryCannotBeReopened() {
        AttMonthlySummary summary = AttMonthlySummary.createOpen(
                1L, 10L, "2026-07", 20, 1, BigDecimal.ZERO, 9600, 0, 0, 0);

        assertThrows(BizException.class, summary::reopen);
    }
}
