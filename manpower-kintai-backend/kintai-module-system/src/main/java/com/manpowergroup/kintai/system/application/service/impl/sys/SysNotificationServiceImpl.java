package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.utils.PageUtil;
import com.manpowergroup.kintai.system.application.assembler.sys.SysNotificationAssembler;
import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.SysNotificationResponse;
import com.manpowergroup.kintai.system.application.service.sys.SysNotificationService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysNotificationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysNotificationServiceImpl extends ServiceImpl<SysNotificationMapper, SysNotification> implements SysNotificationService {

    @Override
    @Transactional
    public SysNotification create(SysNotificationCreateCommand command) {
        SysNotification notification = SysNotification.create(
                command.companyId(), command.recipientId(), command.type(), command.title(),
                command.content(), command.refType(), command.refId());
        save(notification);
        return notification;
    }

    @Override
    public Long countUnread(Long recipientId) {
        return this.lambdaQuery()
                .eq(SysNotification::getRecipientId, recipientId)
                .eq(SysNotification::getIsRead, false)
                .count();
    }

    @Override
    public PageResult<SysNotificationResponse> pageUnread(Long recipientId, PageRequest pageRequest) {
        Page<SysNotification> page = this.lambdaQuery()
                .eq(SysNotification::getRecipientId, recipientId)
                .eq(SysNotification::getIsRead, false)
                .orderByDesc(SysNotification::getCreatedAt)
                .page(PageUtil.toPage(pageRequest));

        return PageResult.of(page, SysNotificationAssembler::toResponse);
    }

    @Override
    @Transactional
    public void markAsRead(Long recipientId, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<SysNotification> notifications = lambdaQuery()
                .eq(SysNotification::getRecipientId, recipientId)
                .in(SysNotification::getId, ids)
                .list();
        List<SysNotification> unread = notifications.stream()
                .filter(notification -> !Boolean.TRUE.equals(notification.getIsRead()))
                .toList();
        unread.forEach(SysNotification::markAsRead);
        if (!unread.isEmpty()) {
            updateBatchById(unread);
        }
    }
}
