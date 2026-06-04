package com.manpowergroup.kintai.system.application.command.sys;

public record MenuUpdateCommand(
        Long parentId,
        String name,
        String code,
        String path,
        String component,
        String icon,
        Integer type,
        Integer sort,
        Integer visible
) {
}
