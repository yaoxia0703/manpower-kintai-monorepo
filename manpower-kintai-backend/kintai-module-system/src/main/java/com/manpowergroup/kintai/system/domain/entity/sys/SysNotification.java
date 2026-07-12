package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.system.domain.enums.NotificationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 通知
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("sys_notification")
/** 社員宛て通知と既読状態を管理する。 */
public class SysNotification {

    @TableId(type = IdType.AUTO)
    @Setter
    // 通知ID
    private Long id;

    // 会社ID
    private Long companyId;

    // 受信者社員ID
    private Long recipientId;

    // 通知タイプ
    private NotificationType type;

    // 通知タイトル
    private String title;

    // 通知内容
    private String content;

    // 関連業務タイプ（REQUEST_TYPE参照）
    private String refType;

    // 関連業務ID
    private Long refId;

    // 既読フラグ
    private Boolean isRead;

    // 既読日時
    private LocalDateTime readAt;

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 更新者ID
    private Long updatedBy;

    // 更新日時
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 論理削除（0=有効 1=削除）
    @TableLogic
    private Integer isDeleted;

    /** 未読状態の通知を作成する。 */
    public static SysNotification create(Long companyId, Long recipientId, NotificationType type,
                                         String title, String content, String refType, Long refId) {
        return new SysNotification()
                .setCompanyId(companyId)
                .setRecipientId(recipientId)
                .setType(type)
                .setTitle(title)
                .setContent(content)
                .setRefType(refType)
                .setRefId(refId)
                .setIsRead(false)
                .setReadAt(null);
    }

    // 既読にする（ドメインメソッド）
    /** 初回の既読日時を保持したまま通知を既読にする。 */
    public void markAsRead() {
        if (Boolean.TRUE.equals(this.isRead)) {
            return;
        }
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}
