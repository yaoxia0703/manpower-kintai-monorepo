package com.manpowergroup.kintai.system.application.command.sys;

import com.manpowergroup.kintai.common.enums.Status;

public record EnumValueUpdateCommand(
        // Enumタイプコード
        String enumTypeCode,

        // Enum値コード
        String code,

        // 表示順
        Integer sort,

        // ステータス
        Status status
) {
}
