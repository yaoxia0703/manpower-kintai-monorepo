package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ApprovalManagerEligibilityTest {

    @Test
    void acceptsOnlyEnabledApprovalReadPermission() {
        SysPermissionService permissionService = Mockito.mock(SysPermissionService.class);
        ApprovalManagerEligibility eligibility = new ApprovalManagerEligibility(permissionService);
        SysPermission enabled = permission("manager:approval:read");
        SysPermission disabled = permission("manager:approval:read");
        disabled.disable();

        when(permissionService.listByEmployeeId(20L)).thenReturn(List.of(enabled));
        when(permissionService.listByEmployeeId(21L)).thenReturn(List.of(disabled));
        when(permissionService.listByEmployeeId(22L)).thenReturn(List.of(permission("employee:request:read")));

        assertTrue(eligibility.canApprove(20L));
        assertFalse(eligibility.canApprove(21L));
        assertFalse(eligibility.canApprove(22L));
    }

    private SysPermission permission(String code) {
        return SysPermission.create(null, code, code, "GET", "/test", null, 1);
    }
}
