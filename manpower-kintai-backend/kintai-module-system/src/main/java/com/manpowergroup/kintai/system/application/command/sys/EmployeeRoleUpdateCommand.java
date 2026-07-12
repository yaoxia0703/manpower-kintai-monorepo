package com.manpowergroup.kintai.system.application.command.sys;

import java.time.LocalDate;

public record EmployeeRoleUpdateCommand(
        // 社員ID
        Long employeeId,

        // ロールID
        Long roleId,

        // 会社ID
        Long companyId,

        // 適用開始日
        LocalDate startDate,

        // 適用終了日
        LocalDate endDate
) {
}
