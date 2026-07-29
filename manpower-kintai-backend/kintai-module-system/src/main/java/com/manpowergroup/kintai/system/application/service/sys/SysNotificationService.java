package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;

public interface SysNotificationService {

    SysNotification create(SysNotificationCreateCommand command);

    Long countUnread(Long recipientId);

    PageResult<SysNotification> pageUnread(Long recipientId, PageRequest pageRequest);

    void markAsRead(Long recipientId, java.util.List<Long> ids);
}
