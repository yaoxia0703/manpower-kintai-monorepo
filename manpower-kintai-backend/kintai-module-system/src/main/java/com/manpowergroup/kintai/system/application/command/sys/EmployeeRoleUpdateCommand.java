package com.manpowergroup.kintai.system.application.command.sys;

import java.time.LocalDate;

public record EmployeeRoleUpdateCommand(
        Long employeeId,
        Long roleId,
        Long companyId,
        LocalDate startDate,
        LocalDate endDate
) {
}
