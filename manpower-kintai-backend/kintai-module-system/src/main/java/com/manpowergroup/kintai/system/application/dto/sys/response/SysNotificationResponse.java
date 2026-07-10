package com.manpowergroup.kintai.system.application.dto.sys.response;

import com.manpowergroup.kintai.system.domain.entity.sys.SysNotification;
import com.manpowergroup.kintai.system.domain.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SysNotificationResponse {

    private Long id;
    private NotificationType type;
    private String title;
    private String content;
    private String refType;
    private Long refId;
    private Boolean isRead;
    private LocalDateTime readAt;
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