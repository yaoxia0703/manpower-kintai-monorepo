package com.manpowergroup.kintai.system.application.command.sys;

public record RoleUpdateCommand(
        // 会社ID
        Long companyId,

        // ロールコード
        String code,

        // ロール名
        String name,

        // 備考
        String remark,

        // 表示順
        Integer sort
) {
}
