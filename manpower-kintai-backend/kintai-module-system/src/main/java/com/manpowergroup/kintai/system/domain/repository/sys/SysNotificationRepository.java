package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;

import java.util.List;

public interface SysNotificationRepository {

    void save(SysNotification sysNotification);

    Long countUnreadByRecipientId(Long recipientId);

    PageResult<SysNotification> pageUnreadByRecipientIdOrderByCreatedAt(Long recipientId, int page,int size);

    List<SysNotification> listByIdsAndRecipientId(List<Long> ids,Long recipientId);

    void updateBatchById(List<SysNotification> notifications);
}
