package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.system.domain.enums.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 通知
@Data
@Accessors(chain = true)
@TableName("sys_notification")
public class SysNotification {

    @TableId(type = IdType.AUTO)
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

    // 既読にする（ドメインメソッド）
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}
