package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.request.SysNotificationCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.response.SysNotificationResponse;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;

public final class SysNotificationAssembler {

    private SysNotificationAssembler() {
    }

    /**
     * 入力データを通知作成コマンドへ変換する。
     *
     * @param request 変換対象の通知作成リクエスト
     * @return 変換後の通知作成コマンド
     */
    public static SysNotificationCreateCommand toCommand(SysNotificationCreateRequest request) {
        return new SysNotificationCreateCommand(
                request.companyId(), request.recipientId(), request.type(),
                request.title(), request.content(), request.refType(), request.refId()
        );
    }

    /**
     * 入力データを通知レスポンスへ変換する。
     *
     * @param notification 処理対象の通知
     * @return 変換後の通知レスポンス
     */
    public static SysNotificationResponse toResponse(SysNotification notification) {
        return SysNotificationResponse.from(notification);
    }
}