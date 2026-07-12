package com.manpowergroup.kintai.system.domain.entity.sys;

import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SysEmployeeRoleTest {

    @Test
    void assignmentIsEffectiveOnInclusiveDateBoundaries() {
        LocalDate startDate = LocalDate.of(2026, 7, 1);
        LocalDate endDate = LocalDate.of(2026, 7, 31);
        SysEmployeeRole assignment = SysEmployeeRole.assign(1L, 2L, 3L, startDate, endDate);

        assertTrue(assignment.isEffectiveOn(startDate));
        assertTrue(assignment.isEffectiveOn(endDate));
        assertFalse(assignment.isEffectiveOn(startDate.minusDays(1)));
        assertFalse(assignment.isEffectiveOn(endDate.plusDays(1)));
    }

    @Test
    void nullDatesCreateOpenEndedAssignment() {
        SysEmployeeRole assignment = SysEmployeeRole.assign(1L, 2L, 3L, null, null);

        assertTrue(assignment.isEffectiveOn(LocalDate.of(2000, 1, 1)));
        assertTrue(assignment.isEffectiveOn(LocalDate.of(2100, 1, 1)));
    }

    @Test
    void assignmentRejectsReversedDateRange() {
        LocalDate startDate = LocalDate.of(2026, 7, 2);
        LocalDate endDate = LocalDate.of(2026, 7, 1);

        assertThrows(BizException.class,
                () -> SysEmployeeRole.assign(1L, 2L, 3L, startDate, endDate));
    }

    @Test
    void changingValidityUsesSameDateRangeRule() {
        SysEmployeeRole assignment = SysEmployeeRole.assign(1L, 2L, 3L, null, null);
        LocalDate startDate = LocalDate.of(2026, 8, 1);
        LocalDate endDate = LocalDate.of(2026, 8, 31);

        assignment.changeValidity(startDate, endDate);

        assertEquals(startDate, assignment.getStartDate());
        assertEquals(endDate, assignment.getEndDate());
        assertThrows(BizException.class,
                () -> assignment.changeValidity(endDate, startDate));
    }
}
