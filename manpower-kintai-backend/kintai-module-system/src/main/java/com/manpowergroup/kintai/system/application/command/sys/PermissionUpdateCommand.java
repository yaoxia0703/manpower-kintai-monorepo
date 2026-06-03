package com.manpowergroup.kintai.system.application.command.sys;

public record PermissionUpdateCommand(
        Long menuId,
        String code,
        String name,
        String method,
        String path,
        String remark,
        Integer sort
) {
}
