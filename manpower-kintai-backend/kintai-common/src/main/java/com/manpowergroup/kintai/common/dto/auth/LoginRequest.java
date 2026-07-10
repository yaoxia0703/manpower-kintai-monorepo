package com.manpowergroup.kintai.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// ログインリクエストDTO
@Data
@Schema(description = "ログインリクエスト")
public class LoginRequest {

    // メールアドレス
    @Email
    @NotBlank
    @Schema(description = "メールアドレス")
    private String email;

    // パスワード
    @NotBlank
    @Schema(description = "パスワード")
    private String password;
}
