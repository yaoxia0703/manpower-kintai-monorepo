package com.manpowergroup.kintai.system.application.command.sys;

import com.manpowergroup.kintai.common.enums.Status;

public record EnumValueUpdateCommand(
        String enumTypeCode,
        String code,
        Integer sort,
        Status status
) {
}
