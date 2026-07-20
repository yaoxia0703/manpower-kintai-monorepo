package com.manpowergroup.kintai.employee.application.dto.emp.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Account作成リクエスト")
public class AccountCreateRequest {

    @NotNull(message = "社員IDは必須です")
    @Schema(description = "社員ID")
    private Long employeeId;

    @NotBlank(message = "ユーザー名は必須です")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    @Schema(description = "ユーザー名")
    private String username;

    @NotBlank(message = "初期パスワードは必須です")
    @Size(min = 8, max = 100, message = "初期パスワードは8文字以上100文字以内で入力してください")
    @Schema(description = "パスワード")
    private String password;
}
