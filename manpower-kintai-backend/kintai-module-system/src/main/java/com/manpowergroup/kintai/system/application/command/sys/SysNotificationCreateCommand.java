package com.manpowergroup.kintai.system.application.command.sys;

import com.manpowergroup.kintai.system.domain.enums.NotificationType;

public record SysNotificationCreateCommand(
        // 会社ID
        Long companyId,

        // 通知受信者社員ID
        Long recipientId,

        // 通知種別
        NotificationType type,

        // 通知タイトル
        String title,

        // 通知本文
        String content,

        // 参照種別
        String refType,

        // 参照ID
        Long refId
) {
}
