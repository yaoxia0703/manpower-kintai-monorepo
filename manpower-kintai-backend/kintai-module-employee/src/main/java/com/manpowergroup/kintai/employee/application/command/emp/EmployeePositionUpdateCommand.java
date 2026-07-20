package com.manpowergroup.kintai.employee.application.command.emp;

import com.manpowergroup.kintai.common.enums.Status;

import java.time.LocalDate;

public record EmployeePositionUpdateCommand(
        // 社員ID
        Long employeeId,

        // 会社ID
        Long companyId,

        // 組織ノードID
        Long nodeId,

        // 等級ID
        Long gradeId,

        // 主所属フラグ
        Integer isPrimary,

        // 適用開始日
        LocalDate startDate,

        // 適用終了日
        LocalDate endDate,

        // ステータス
        Status status
) {
}
