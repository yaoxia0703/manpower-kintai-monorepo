package com.manpowergroup.kintai.system.application.command.sys;

public record PermissionCreateCommand(
        // メニューID
        Long menuId,

        // 権限コード
        String code,

        // 権限名
        String name,

        // HTTPメソッド
        String method,

        // APIパス
        String path,

        // 備考
        String remark,

        // 表示順
        Integer sort
) {
}
