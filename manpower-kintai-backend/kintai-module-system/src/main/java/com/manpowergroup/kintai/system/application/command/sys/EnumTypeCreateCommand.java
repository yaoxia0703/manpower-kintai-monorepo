package com.manpowergroup.kintai.system.application.command.sys;

import com.manpowergroup.kintai.common.enums.Status;

public record EnumTypeCreateCommand(
        // Enumタイプコード
        String code,

        // Enumタイプ名
        String name,

        // 備考
        String remark,

        // 表示順
        Integer sort,

        // ステータス
        Status status
) {
}
