package com.manpowergroup.kintai.system.application.command.sys;

import com.manpowergroup.kintai.common.enums.Status;

public record EnumTypeUpdateCommand(
        String code,
        String name,
        String remark,
        Integer sort,
        Status status
) {
}
