package com.manpowergroup.kintai.system.application.command.sys;

import com.manpowergroup.kintai.common.enums.PermissionHttpMethod;

public record PermissionUpdateCommand(
        // メニューID
        Long menuId,

        // 権限コード
        String code,

        // 権限名
        String name,

        // HTTPメソッド
        PermissionHttpMethod method,

        // APIパス
        String path,

        // 備考
        String remark,

        // 表示順
        Integer sort
) {
}
