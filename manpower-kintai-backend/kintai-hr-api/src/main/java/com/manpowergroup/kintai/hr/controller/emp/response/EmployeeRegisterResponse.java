package com.manpowergroup.kintai.hr.controller.emp.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "新社員登録リスボン")
public record EmployeeRegisterResponse(

    @Schema(description = "社員ID")
    Long employeeId,
    @Schema(description = "アカウントID")
    Long accountId,
    @Schema(description = "職位ID")
    Long positionId,
    @Schema(description = "社員番号")
    String employeeCode,
    @Schema(description = "表示名")
    String displayName,
    @Schema(description = "メールアドレス")
    String email

) {
}
