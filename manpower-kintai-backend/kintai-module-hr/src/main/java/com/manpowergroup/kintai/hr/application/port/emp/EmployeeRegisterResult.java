package com.manpowergroup.kintai.hr.application.port.emp;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "新社員登録戻り値")
public record EmployeeRegisterResult(
    Long employeeId,
    Long accountId,
    Long positionId,
    String employeeCode,
    String displayName,
    String email
) {

}
