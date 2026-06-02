package com.manpowergroup.kintai.system.application.dto.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleAuthorizationResponse {

    private List<MenuResponse> menus;

    private List<SysPermission> permissions;

    private List<Long> selectedMenuIds;

    private List<Long> selectedPermissionIds;
}
