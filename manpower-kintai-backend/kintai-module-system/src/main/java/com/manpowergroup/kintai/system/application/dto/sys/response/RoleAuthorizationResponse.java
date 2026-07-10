package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "RoleAuthorizationレスポンス")
public class RoleAuthorizationResponse {

    @Schema(description = "メニューリスト")
    private List<MenuResponse> menus;

    @Schema(description = "権限リスト")
    private List<PermissionResponse> permissions;

    @Schema(description = "選択済みメニューIDリスト")
    private List<Long> selectedMenuIds;

    @Schema(description = "選択済み権限IDリスト")
    private List<Long> selectedPermissionIds;
}
