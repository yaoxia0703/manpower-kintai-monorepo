package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttPaidLeaveBalanceTest {

    @Test
    void grantInitializesUsedExpiredAndBalanceDays() {
        AttPaidLeaveBalance balance = AttPaidLeaveBalance.grant(
                1L,
                10L,
                "2026",
                BigDecimal.valueOf(20),
                LocalDate.of(2028, 3, 31));

        assertEquals(1L, balance.getEmployeeId());
        assertEquals(10L, balance.getCompanyId());
        assertEquals("2026", balance.getFiscalYear());
        assertEquals(BigDecimal.valueOf(20), balance.getGrantedDays());
        assertEquals(BigDecimal.ZERO, balance.getUsedDays());
        assertEquals(BigDecimal.ZERO, balance.getExpiredDays());
        assertEquals(BigDecimal.valueOf(20), balance.getBalanceDays());
        assertEquals(LocalDate.of(2028, 3, 31), balance.getExpireDate());
    }

    @Test
    void useDaysConsumesBalance() {
        AttPaidLeaveBalance balance = AttPaidLeaveBalance.grant(
                1L,
                10L,
                "2026",
                BigDecimal.valueOf(20),
                LocalDate.of(2028, 3, 31));

        balance.useDays(BigDecimal.valueOf(1.5));

        assertEquals(BigDecimal.valueOf(1.5), balance.getUsedDays());
        assertEquals(BigDecimal.valueOf(18.5), balance.getBalanceDays());
    }

    @Test
    void useDaysRejectsMoreThanBalance() {
        AttPaidLeaveBalance balance = AttPaidLeaveBalance.grant(
                1L,
                10L,
                "2026",
                BigDecimal.valueOf(2),
                LocalDate.of(2028, 3, 31));

        assertThrows(BizException.class, () -> balance.useDays(BigDecimal.valueOf(3)));
    }

    @Test
    void expireDaysConsumesBalanceAndTracksExpiredDays() {
        AttPaidLeaveBalance balance = AttPaidLeaveBalance.grant(
                1L,
                10L,
                "2026",
                BigDecimal.valueOf(20),
                LocalDate.of(2028, 3, 31));

        balance.expireDays(BigDecimal.valueOf(4));

        assertEquals(BigDecimal.valueOf(4), balance.getExpiredDays());
        assertEquals(BigDecimal.valueOf(16), balance.getBalanceDays());
    }

    @Test
    void expireDaysRejectsMoreThanBalance() {
        AttPaidLeaveBalance balance = AttPaidLeaveBalance.grant(
                1L,
                10L,
                "2026",
                BigDecimal.valueOf(2),
                LocalDate.of(2028, 3, 31));

        assertThrows(BizException.class, () -> balance.expireDays(BigDecimal.valueOf(3)));
    }
}
