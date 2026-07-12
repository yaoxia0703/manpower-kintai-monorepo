package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.SysNotificationResponse;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;

public interface SysNotificationService extends IService<SysNotification> {

    SysNotification create(SysNotificationCreateCommand command);

    Long countUnread(Long recipientId);

    PageResult<SysNotificationResponse> pageUnread(Long recipientId, PageRequest pageRequest);

    void markAsRead(Long recipientId, java.util.List<Long> ids);
}
