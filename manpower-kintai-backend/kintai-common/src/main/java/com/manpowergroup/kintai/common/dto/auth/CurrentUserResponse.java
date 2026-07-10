package com.manpowergroup.kintai.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "現在ユーザーレスポンス")
public class CurrentUserResponse {

    @Schema(description = "ユーザー情報")
    private UserProfile user;

    @Schema(description = "ロールリスト")
    private List<String> roles;

    @Schema(description = "権限リスト")
    private List<String> permissions;

    @Schema(description = "メニューリスト")
    private List<MenuItem> menus;

    @Data
    @Builder
    public static class UserProfile {
        @Schema(description = "社員ID")
        private Long employeeId;
        @Schema(description = "アカウントID")
        private Long accountId;
        @Schema(description = "会社ID")
        private Long companyId;
        @Schema(description = "社員番号")
        private String employeeCode;
        @Schema(description = "表示名")
        private String displayName;
        @Schema(description = "メールアドレス")
        private String email;
    }

    @Data
    @Builder
    public static class MenuItem {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "親ID")
        private Long parentId;
        @Schema(description = "名称")
        private String name;
        @Schema(description = "コード")
        private String code;
        @Schema(description = "パス")
        private String path;
        @Schema(description = "コンポーネント")
        private String component;
        @Schema(description = "アイコン")
        private String icon;
        @Schema(description = "タイプ")
        private Integer type;
        @Schema(description = "表示順")
        private Integer sort;
        @Schema(description = "表示フラグ")
        private Integer visible;
    }
}
