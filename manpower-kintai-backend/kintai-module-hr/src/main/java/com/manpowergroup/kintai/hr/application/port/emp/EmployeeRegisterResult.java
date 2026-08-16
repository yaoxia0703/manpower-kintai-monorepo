package com.manpowergroup.kintai.hr.application.port.emp;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "新社員登録戻り値")
public record EmployeeRegisterResult(
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
