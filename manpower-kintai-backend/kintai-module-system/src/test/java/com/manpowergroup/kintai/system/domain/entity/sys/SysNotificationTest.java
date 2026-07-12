package com.manpowergroup.kintai.system.domain.entity.sys;

import com.manpowergroup.kintai.system.domain.enums.NotificationType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SysNotificationTest {

    @Test
    void createStartsNotificationAsUnread() {
        SysNotification notification = SysNotification.create(
                10L, 20L, NotificationType.REQUEST_SUBMITTED,
                "Submitted", "Request submitted", "LEAVE", 30L);

        assertEquals(10L, notification.getCompanyId());
        assertEquals(20L, notification.getRecipientId());
        assertEquals(NotificationType.REQUEST_SUBMITTED, notification.getType());
        assertFalse(notification.getIsRead());
        assertNull(notification.getReadAt());
    }

    @Test
    void markAsReadIsIdempotentAndKeepsFirstReadTime() {
        SysNotification notification = SysNotification.create(
                10L, 20L, NotificationType.REQUEST_APPROVED,
                "Approved", null, "LEAVE", 30L);

        notification.markAsRead();
        LocalDateTime firstReadAt = notification.getReadAt();
        notification.markAsRead();

        assertTrue(notification.getIsRead());
        assertEquals(firstReadAt, notification.getReadAt());
    }

    @Test
    void businessFieldsDoNotExposePublicSetters() {
        Set<String> setters = Set.of(
                "setCompanyId", "setRecipientId", "setType", "setTitle", "setContent",
                "setRefType", "setRefId", "setIsRead", "setReadAt");

        boolean exposed = Arrays.stream(SysNotification.class.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));

        assertFalse(exposed);
    }
}
