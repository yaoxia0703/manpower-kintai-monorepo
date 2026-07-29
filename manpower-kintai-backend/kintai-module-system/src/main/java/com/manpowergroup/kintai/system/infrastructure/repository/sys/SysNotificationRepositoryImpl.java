package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;
import com.manpowergroup.kintai.system.domain.repository.sys.SysNotificationRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SysNotificationRepositoryImpl implements SysNotificationRepository {

    private final SysNotificationMapper notificationMapper;


    @Override
    public void save(SysNotification sysNotification) {
        notificationMapper.insert(sysNotification);
    }

    @Override
    public Long countUnreadByRecipientId(Long recipientId) {
        return notificationMapper.selectCount(Wrappers.<SysNotification>lambdaQuery()
            .eq(SysNotification::getRecipientId, recipientId)
            .eq(SysNotification::getIsRead, false)
        );
    }

    @Override
    public PageResult<SysNotification> pageUnreadByRecipientIdOrderByCreatedAt(Long recipientId, int page, int size) {
        Page<SysNotification> p = new Page<>(page, size);
        notificationMapper.selectPage(p, Wrappers.<SysNotification>lambdaQuery()
            .eq(SysNotification::getRecipientId, recipientId)
            .eq(SysNotification::getIsRead, false)
            .orderByDesc(SysNotification::getCreatedAt))
        ;
        return PageResult.of(p);
    }

    @Override
    public List<SysNotification> listByIdsAndRecipientId(List<Long> ids, Long recipientId) {
        return notificationMapper.selectList(Wrappers.<SysNotification>lambdaQuery()
            .eq(SysNotification::getRecipientId, recipientId)
            .in(SysNotification::getId, ids)
        );
    }

    @Override
    public void updateBatchById(List<SysNotification> notifications) {
        notifications.forEach(notificationMapper::updateById);
    }
}
