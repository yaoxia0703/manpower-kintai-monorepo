package com.manpowergroup.kintai.system.domain.service.sys;

import com.manpowergroup.kintai.system.domain.model.sys.RoleAuthorization;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleAuthorizationDomainServiceTest {

    @Test
    void replaceAuthorizationReturnsNormalizedAuthorizationSnapshot() {
        RoleAuthorizationDomainService service = new RoleAuthorizationDomainService();

        RoleAuthorization authorization = service.replaceAuthorization(
                7L,
                Arrays.asList(1L, null, 2L, 1L),
                Arrays.asList(10L, null, 11L, 10L));

        assertEquals(7L, authorization.roleId());
        assertEquals(List.of(1L, 2L), authorization.menuIds());
        assertEquals(List.of(10L, 11L), authorization.permissionIds());
    }
}
