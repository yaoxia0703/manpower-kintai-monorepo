package com.manpowergroup.kintai.system.application.command.sys;

import java.util.List;

public record RoleAuthorizationSaveCommand(
        Long roleId,
        List<Long> menuIds,
        List<Long> permissionIds
) {
}
