package com.manpowergroup.kintai.employee.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.application.dto.sys.request.SysNotificationMarkReadRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysNotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.verify;

class EmpNotificationControllerTest {

    @Test
    void unreadQueriesUseAuthenticatedEmployee() {
        SysNotificationService service = Mockito.mock(SysNotificationService.class);
        EmpNotificationController controller = new EmpNotificationController(service);
        LoginPrincipal principal = new LoginPrincipal(20L, 200L);
        PageRequest pageRequest = new PageRequest(1, 20);

        controller.countUnread(principal);
        controller.listUnread(principal, pageRequest);

        verify(service).countUnread(20L);
        verify(service).pageUnread(20L, pageRequest);
    }

    @Test
    void markReadIsRestrictedToAuthenticatedEmployee() {
        SysNotificationService service = Mockito.mock(SysNotificationService.class);
        EmpNotificationController controller = new EmpNotificationController(service);

        controller.markAsRead(
                new LoginPrincipal(20L, 200L),
                new SysNotificationMarkReadRequest(List.of(1L, 2L)));

        verify(service).markAsRead(20L, List.of(1L, 2L));
    }
}
