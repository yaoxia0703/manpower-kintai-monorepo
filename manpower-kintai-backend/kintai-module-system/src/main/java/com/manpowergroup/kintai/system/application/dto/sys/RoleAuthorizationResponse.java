package com.manpowergroup.kintai.system.application.dto.sys;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleAuthorizationResponse {

    private List<MenuResponse> menus;

    private List<PermissionResponse> permissions;

    private List<Long> selectedMenuIds;

    private List<Long> selectedPermissionIds;
}
