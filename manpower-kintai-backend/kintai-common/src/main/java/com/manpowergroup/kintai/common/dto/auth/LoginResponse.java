package com.manpowergroup.kintai.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

// ログインレスポンスDTO
@Data
@Builder
@Schema(description = "ログインレスポンス")
public class LoginResponse {

    // JWTトークン
    @Schema(description = "token")
    private String token;

    // 社員ID
    @Schema(description = "社員ID")
    private Long employeeId;

    // アカウントID
    @Schema(description = "アカウントID")
    private Long accountId;

    // 表示名（姓 名）
    @Schema(description = "表示名")
    private String displayName;

    // メールアドレス
    @Schema(description = "メールアドレス")
    private String email;
}
