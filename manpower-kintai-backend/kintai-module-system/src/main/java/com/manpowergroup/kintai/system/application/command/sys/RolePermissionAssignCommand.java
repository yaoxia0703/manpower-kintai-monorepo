package com.manpowergroup.kintai.system.application.command.sys;

import java.util.List;

public record RolePermissionAssignCommand(
        // ロールID
        Long roleId,

        // 権限IDリスト
        List<Long> permissionIds
) {
}
