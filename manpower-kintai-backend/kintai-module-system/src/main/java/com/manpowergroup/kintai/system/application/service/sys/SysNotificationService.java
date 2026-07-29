package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;

public interface SysNotificationService {

    /**
     * 通知を新規作成する。
     *
     * @param command 処理対象の通知作成コマンド
     * @return 保存した通知
     */
    SysNotification create(SysNotificationCreateCommand command);

    /**
     * 対象社員の未読通知件数を取得する。
     *
     * @param recipientId 通知先の社員ID
     * @return 未読通知件数
     */
    Long countUnread(Long recipientId);

    /**
     * 対象社員の未読通知をページング取得する。
     *
     * @param recipientId 通知先の社員ID
     * @param pageRequest ページング条件
     * @return ページングされた通知一覧
     */
    PageResult<SysNotification> pageUnread(Long recipientId, PageRequest pageRequest);

    /**
     * 指定された通知を既読に更新する。
     *
     * @param recipientId 通知先の社員ID
     * @param ids 対象ID一覧
     */
    void markAsRead(Long recipientId, java.util.List<Long> ids);
}
