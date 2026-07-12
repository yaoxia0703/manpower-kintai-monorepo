package com.manpowergroup.kintai.attendance.domain.entity.att;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

class AttendanceEntityEncapsulationTest {

    @Test
    void recordDoesNotExposeBusinessSetters() {
        assertNoPublicSetters(AttRecord.class, Set.of(
                "setEmployeeId", "setCompanyId", "setWorkDate", "setClockIn", "setClockOut",
                "setAttendanceType", "setWorkMinutes", "setOvertimeMinutes", "setRemark", "setStatus"));
    }

    @Test
    void paidLeaveBalanceDoesNotExposeBusinessSetters() {
        assertNoPublicSetters(AttPaidLeaveBalance.class, Set.of(
                "setEmployeeId", "setCompanyId", "setFiscalYear", "setGrantedDays", "setUsedDays",
                "setExpiredDays", "setBalanceDays", "setExpireDate"));
    }

    @Test
    void monthlySummaryDoesNotExposeBusinessSetters() {
        assertNoPublicSetters(AttMonthlySummary.class, Set.of(
                "setEmployeeId", "setCompanyId", "setSummaryMonth", "setWorkDays", "setAbsentDays",
                "setPaidLeaveDays", "setTotalWorkMinutes", "setTotalOvertimeMinutes", "setLateCount",
                "setEarlyLeaveCount", "setStatus", "setConfirmedAt", "setConfirmedBy"));
    }

    private void assertNoPublicSetters(Class<?> type, Set<String> setters) {
        boolean exposed = Arrays.stream(type.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));
        assertFalse(exposed);
    }
}
