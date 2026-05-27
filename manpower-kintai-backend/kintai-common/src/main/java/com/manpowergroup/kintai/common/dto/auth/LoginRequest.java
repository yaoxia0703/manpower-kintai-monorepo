package com.manpowergroup.kintai.common.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// ログインリクエストDTO
@Data
public class LoginRequest {

    // メールアドレス
    @Email
    @NotBlank
    private String email;

    // パスワード
    @NotBlank
    private String password;
}
