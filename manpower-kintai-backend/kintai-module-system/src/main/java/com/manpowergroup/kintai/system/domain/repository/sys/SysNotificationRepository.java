package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;

import java.util.List;

public interface SysNotificationRepository {

    /**
     * 通知を保存する。
     *
     * @param sysNotification 保存対象の通知
     */
    void save(SysNotification sysNotification);

    /**
     * 通知先IDに一致する通知の件数を取得する。
     *
     * @param recipientId 通知先の社員ID
     * @return 該当件数
     */
    Long countUnreadByRecipientId(Long recipientId);

    /**
     * 指定された検索条件で通知をページング取得する。
     *
     * @param recipientId 通知先の社員ID
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた通知一覧
     */
    PageResult<SysNotification> pageUnreadByRecipientIdOrderByCreatedAt(Long recipientId, int page,int size);

    /**
     * ID一覧および通知先IDに一致する通知を一覧取得する。
     *
     * @param ids 対象ID一覧
     * @param recipientId 通知先の社員ID
     * @return 通知一覧。該当しない場合は空リスト
     */
    List<SysNotification> listByIdsAndRecipientId(List<Long> ids,Long recipientId);

    /**
     * 通知を更新する。
     *
     * @param notifications 保存対象の通知一覧
     */
    void updateBatchById(List<SysNotification> notifications);
}
