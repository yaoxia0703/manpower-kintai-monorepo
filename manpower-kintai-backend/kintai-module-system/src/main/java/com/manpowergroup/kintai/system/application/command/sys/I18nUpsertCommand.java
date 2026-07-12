package com.manpowergroup.kintai.system.application.command.sys;

public record I18nUpsertCommand(
        // 参照種別
        String refType,

        // 参照ID
        Long refId,

        // 言語コード
        String language,

        // 翻訳内容
        String content
) {
}
