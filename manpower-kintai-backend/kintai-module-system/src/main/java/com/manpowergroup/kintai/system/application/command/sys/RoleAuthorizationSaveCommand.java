package com.manpowergroup.kintai.system.application.command.sys;

import java.util.List;

public record RoleAuthorizationSaveCommand(
        // ロールID
        Long roleId,

        // メニューIDリスト
        List<Long> menuIds,

        // 権限IDリスト
        List<Long> permissionIds
) {
}
