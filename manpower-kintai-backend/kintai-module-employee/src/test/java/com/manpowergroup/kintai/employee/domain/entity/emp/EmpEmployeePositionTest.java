package com.manpowergroup.kintai.employee.domain.entity.emp;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmpEmployeePositionTest {

    @Test
    void assignDefaultsStatusToEnabledWhenStatusIsNotSpecified() {
        LocalDate startDate = LocalDate.of(2026, 4, 1);

        EmpEmployeePosition position = EmpEmployeePosition.assign(
                1L,
                2L,
                3L,
                4L,
                1,
                startDate,
                null,
                null);

        assertEquals(1L, position.getEmployeeId());
        assertEquals(2L, position.getCompanyId());
        assertEquals(3L, position.getNodeId());
        assertEquals(4L, position.getGradeId());
        assertEquals(1, position.getIsPrimary());
        assertEquals(startDate, position.getStartDate());
        assertEquals(Status.ENABLED, position.getStatus());
    }

    @Test
    void updateAssignmentKeepsEmployeeCompanyAndStatus() {
        LocalDate startDate = LocalDate.of(2026, 4, 1);
        EmpEmployeePosition position = EmpEmployeePosition.assign(
                1L,
                2L,
                3L,
                4L,
                1,
                startDate,
                null,
                Status.DISABLED)
                .setId(99L);

        LocalDate newStartDate = LocalDate.of(2026, 5, 1);
        LocalDate newEndDate = LocalDate.of(2027, 3, 31);
        position.updateAssignment(30L, 40L, 0, newStartDate, newEndDate);

        assertEquals(99L, position.getId());
        assertEquals(1L, position.getEmployeeId());
        assertEquals(2L, position.getCompanyId());
        assertEquals(Status.DISABLED, position.getStatus());
        assertEquals(30L, position.getNodeId());
        assertEquals(40L, position.getGradeId());
        assertEquals(0, position.getIsPrimary());
        assertEquals(newStartDate, position.getStartDate());
        assertEquals(newEndDate, position.getEndDate());
    }
}
