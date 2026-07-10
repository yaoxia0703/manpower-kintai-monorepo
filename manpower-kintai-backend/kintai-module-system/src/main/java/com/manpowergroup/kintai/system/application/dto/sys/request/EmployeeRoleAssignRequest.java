package com.manpowergroup.kintai.system.application.dto.sys.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "EmployeeRoleAssignリクエスト")
public class EmployeeRoleAssignRequest {

    @NotNull(message = "社員IDは必須です")
    @Schema(description = "社員ID")
    private Long employeeId;

    @NotNull(message = "ロールIDは必須です")
    @Schema(description = "ロールID")
    private Long roleId;

    @NotNull(message = "会社は必須です")
    @Schema(description = "会社ID")
    private Long companyId;

    @NotNull(message = "有効開始日は必須です")
    @Schema(description = "開始日")
    private LocalDate startDate;

    @Schema(description = "終了日")
    private LocalDate endDate;
}
