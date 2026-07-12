package com.manpowergroup.kintai.system.application.command.sys;

import java.util.List;

public record RoleMenuAssignCommand(
        // ロールID
        Long roleId,

        // メニューIDリスト
        List<Long> menuIds
) {
}
