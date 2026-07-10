package com.manpowergroup.kintai.system.application.dto.emp.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Account更新リクエスト")
public class AccountUpdateRequest {

    @NotBlank(message = "ユーザー名は必須です")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    @Schema(description = "ユーザー名")
    private String username;
}
