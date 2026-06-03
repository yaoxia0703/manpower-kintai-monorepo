package com.manpowergroup.kintai.system.application.command.sys;

public record I18nUpsertCommand(
        String refType,
        Long refId,
        String language,
        String content
) {
}
