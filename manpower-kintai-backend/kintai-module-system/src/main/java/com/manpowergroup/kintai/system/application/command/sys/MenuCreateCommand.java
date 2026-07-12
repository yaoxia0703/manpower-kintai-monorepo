package com.manpowergroup.kintai.system.application.command.sys;

public record MenuCreateCommand(
        // 親メニューID
        Long parentId,

        // メニュー名
        String name,

        // メニューコード
        String code,

        // ルートパス
        String path,

        // フロントコンポーネント
        String component,

        // アイコン
        String icon,

        // メニュー種別
        Integer type,

        // 表示順
        Integer sort,

        // 表示フラグ
        Integer visible
) {
}
