package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysNotificationService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;
import com.manpowergroup.kintai.system.domain.repository.sys.SysNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysNotificationServiceImpl implements SysNotificationService {

    private final SysNotificationRepository notificationRepository;

    public SysNotificationServiceImpl(SysNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public SysNotification create(SysNotificationCreateCommand command) {
        SysNotification notification = SysNotification.create(
            command.companyId(), command.recipientId(), command.type(), command.title(),
            command.content(), command.refType(), command.refId());
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public Long countUnread(Long recipientId) {
        return notificationRepository.countUnreadByRecipientId(recipientId);
    }

    @Override
    public PageResult<SysNotification> pageUnread(Long recipientId, PageRequest pageRequest) {
        return notificationRepository.pageUnreadByRecipientIdOrderByCreatedAt(recipientId, pageRequest.page(), pageRequest.size());
    }

    @Override
    @Transactional
    public void markAsRead(Long recipientId, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<SysNotification> notifications =
            notificationRepository.listByIdsAndRecipientId(ids, recipientId);
        List<SysNotification> unread = notifications.stream()
            .filter(notification -> !Boolean.TRUE.equals(notification.getIsRead()))
            .toList();
        unread.forEach(SysNotification::markAsRead);
        if (!unread.isEmpty()) {
            notificationRepository.updateBatchById(unread);
        }
    }
}
