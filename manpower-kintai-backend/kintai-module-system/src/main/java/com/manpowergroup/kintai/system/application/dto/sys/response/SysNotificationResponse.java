package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;
import com.manpowergroup.kintai.system.domain.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "SysNotificationレスポンス")
public class SysNotificationResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "タイプ")
    private NotificationType type;
    @Schema(description = "タイトル")
    private String title;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "関連業務タイプ")
    private String refType;
    @Schema(description = "関連業務ID")
    private Long refId;
    @Schema(description = "既読フラグ")
    private Boolean isRead;
    @Schema(description = "既読日時")
    private LocalDateTime readAt;
    @Schema(description = "作成日時")
    private LocalDateTime createdAt;

    public static SysNotificationResponse from(SysNotification notification) {
        return SysNotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .content(notification.getContent())
                .refType(notification.getRefType())
                .refId(notification.getRefId())
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}