package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.request.SysNotificationCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.response.SysNotificationResponse;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;

public final class SysNotificationAssembler {

    private SysNotificationAssembler() {
    }

    public static SysNotificationCreateCommand toCommand(SysNotificationCreateRequest request) {
        return new SysNotificationCreateCommand(
                request.companyId(), request.recipientId(), request.type(),
                request.title(), request.content(), request.refType(), request.refId()
        );
    }

    public static SysNotificationResponse toResponse(SysNotification notification) {
        return SysNotificationResponse.from(notification);
    }
}