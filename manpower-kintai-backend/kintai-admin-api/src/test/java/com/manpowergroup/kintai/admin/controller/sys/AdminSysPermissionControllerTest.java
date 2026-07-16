package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminSysPermissionControllerTest {

    @Test
    void pageDelegatesMenuKeywordAndPaginationToService() {
        SysPermissionService service = Mockito.mock(SysPermissionService.class);
        AdminSysPermissionController controller = new AdminSysPermissionController(service);
        when(service.page(6L, "admin", PageRequest.of(2, 25)))
                .thenReturn(new PageResult<SysPermission>());

        controller.page(6L, "admin", 2, 25);

        verify(service).page(6L, "admin", PageRequest.of(2, 25));
    }
}
