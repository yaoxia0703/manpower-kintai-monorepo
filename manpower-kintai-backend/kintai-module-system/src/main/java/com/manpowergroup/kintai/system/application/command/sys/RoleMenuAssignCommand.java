package com.manpowergroup.kintai.system.application.command.sys;

import java.util.List;

public record RoleMenuAssignCommand(
        Long roleId,
        List<Long> menuIds
) {
}
