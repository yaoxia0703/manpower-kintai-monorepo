package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysNotificationService;
import com.manpowergroup.kintai.system.domain.enums.NotificationType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class SystemApprovalNotificationAdapterTest {

    private final SysNotificationService notificationService =
            Mockito.mock(SysNotificationService.class);
    private final SystemApprovalNotificationAdapter adapter =
            new SystemApprovalNotificationAdapter(notificationService);

    @Test
    void submittedEventCreatesNotificationForApprover() {
        adapter.requestSubmitted(10L, 20L, "PAID_LEAVE", 99L);

        SysNotificationCreateCommand command = capture();
        assertEquals(10L, command.companyId());
        assertEquals(20L, command.recipientId());
        assertEquals(NotificationType.REQUEST_SUBMITTED, command.type());
        assertEquals("PAID_LEAVE", command.refType());
        assertEquals(99L, command.refId());
    }

    @Test
    void resultEventsMapToMatchingNotificationTypes() {
        adapter.requestApproved(10L, 1L, "PAID_LEAVE", 99L);
        adapter.requestRejected(10L, 1L, "PAID_LEAVE", 99L);
        adapter.requestCancelled(10L, 20L, "PAID_LEAVE", 99L);
        adapter.approvalDelegated(10L, 30L, "PAID_LEAVE", 99L);

        ArgumentCaptor<SysNotificationCreateCommand> captor =
                ArgumentCaptor.forClass(SysNotificationCreateCommand.class);
        verify(notificationService, Mockito.times(4)).create(captor.capture());
        assertEquals(
                java.util.List.of(
                        NotificationType.REQUEST_APPROVED,
                        NotificationType.REQUEST_REJECTED,
                        NotificationType.REQUEST_CANCELLED,
                        NotificationType.REQUEST_SUBMITTED),
                captor.getAllValues().stream().map(SysNotificationCreateCommand::type).toList());
    }

    private SysNotificationCreateCommand capture() {
        ArgumentCaptor<SysNotificationCreateCommand> captor =
                ArgumentCaptor.forClass(SysNotificationCreateCommand.class);
        verify(notificationService).create(captor.capture());
        return captor.getValue();
    }
}
