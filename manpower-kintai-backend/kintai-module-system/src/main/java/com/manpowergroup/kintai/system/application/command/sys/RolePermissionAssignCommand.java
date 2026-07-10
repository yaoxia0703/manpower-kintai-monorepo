package com.manpowergroup.kintai.system.application.command.sys;

import java.util.List;

public record RolePermissionAssignCommand(
        Long roleId,
        List<Long> permissionIds
) {
}
