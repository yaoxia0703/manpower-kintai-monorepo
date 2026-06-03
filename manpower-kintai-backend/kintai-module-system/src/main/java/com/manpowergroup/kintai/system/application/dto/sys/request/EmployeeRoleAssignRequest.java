package com.manpowergroup.kintai.system.application.dto.sys.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeRoleAssignRequest {

    @NotNull(message = "社員IDは必須です")
    private Long employeeId;

    @NotNull(message = "ロールIDは必須です")
    private Long roleId;

    @NotNull(message = "会社は必須です")
    private Long companyId;

    @NotNull(message = "有効開始日は必須です")
    private LocalDate startDate;

    private LocalDate endDate;
}
