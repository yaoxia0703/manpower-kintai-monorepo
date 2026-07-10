package com.manpowergroup.kintai.system.application.command.sys;

import com.manpowergroup.kintai.system.domain.enums.NotificationType;

public record SysNotificationCreateCommand(
        Long companyId,
        Long recipientId,
        NotificationType type,
        String title,
        String content,
        String refType,
        Long refId
) {
}