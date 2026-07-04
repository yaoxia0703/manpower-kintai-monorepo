package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.framework.security.authority.PermissionRule;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PermissionRuleProviderImplTest {

    @Test
    void loadsEnabledPermissionRulesFromSysPermission() {
        SysPermissionMapper permissionMapper = mock(SysPermissionMapper.class);
        when(permissionMapper.selectList(any())).thenReturn(List.of(
                new SysPermission()
                        .setCode("admin:employee:read")
                        .setMethod("GET")
                        .setPath("/admin/emp/employees/**")
                        .setStatus(Status.ENABLED)));
        PermissionRuleProviderImpl provider = new PermissionRuleProviderImpl(permissionMapper);

        List<PermissionRule> rules = provider.loadEnabledRules();

        assertEquals(List.of(new PermissionRule(
                "admin:employee:read",
                "GET",
                "/admin/emp/employees/**")), rules);
    }
}
