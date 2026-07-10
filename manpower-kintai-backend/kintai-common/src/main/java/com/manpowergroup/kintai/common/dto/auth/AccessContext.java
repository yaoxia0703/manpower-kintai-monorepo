package com.manpowergroup.kintai.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "アクセスコンテキスト")
public class AccessContext {

    @Schema(description = "ユーザー情報")
    private CurrentUserResponse.UserProfile user;

    @Schema(description = "ロールリスト")
    private List<String> roles;

    @Schema(description = "権限リスト")
    private List<String> permissions;

    @Schema(description = "認可情報リスト")
    private List<String> authorities;

    @Schema(description = "メニューリスト")
    private List<CurrentUserResponse.MenuItem> menus;
}
