package com.manpowergroup.kintai.system.application.dto.hr.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "EmployeeOnboardingレスポンス")
public class EmployeeOnboardingResponse {

    @Schema(description = "社員ID")
    private Long employeeId;
    @Schema(description = "アカウントID")
    private Long accountId;
    @Schema(description = "職位ID")
    private Long positionId;
    @Schema(description = "社員番号")
    private String employeeCode;
    @Schema(description = "表示名")
    private String displayName;
    @Schema(description = "メールアドレス")
    private String email;
}
