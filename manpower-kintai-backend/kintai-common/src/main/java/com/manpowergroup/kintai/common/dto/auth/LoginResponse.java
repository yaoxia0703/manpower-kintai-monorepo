package com.manpowergroup.kintai.common.dto.auth;

import lombok.Builder;
import lombok.Data;

// ログインレスポンスDTO
@Data
@Builder
public class LoginResponse {

    // JWTトークン
    private String token;

    // 社員ID
    private Long employeeId;

    // アカウントID
    private Long accountId;

    // 表示名（姓 名）
    private String displayName;

    // メールアドレス
    private String email;
}
