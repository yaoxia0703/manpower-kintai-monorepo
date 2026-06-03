package com.manpowergroup.kintai.system.application.command.sys;

public record RoleUpdateCommand(
        Long companyId,
        String code,
        String name,
        String remark,
        Integer sort
) {
}
