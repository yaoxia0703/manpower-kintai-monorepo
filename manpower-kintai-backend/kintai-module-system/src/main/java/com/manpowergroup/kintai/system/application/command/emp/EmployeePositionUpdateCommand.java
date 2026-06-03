package com.manpowergroup.kintai.system.application.command.emp;

import com.manpowergroup.kintai.common.enums.Status;

import java.time.LocalDate;

public record EmployeePositionUpdateCommand(
        Long employeeId,
        Long companyId,
        Long nodeId,
        Long gradeId,
        Integer isPrimary,
        LocalDate startDate,
        LocalDate endDate,
        Status status
) {
}
