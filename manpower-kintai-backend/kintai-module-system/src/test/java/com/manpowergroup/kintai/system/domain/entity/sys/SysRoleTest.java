package com.manpowergroup.kintai.system.domain.entity.sys;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SysRoleTest {

    @Test
    void createDefaultsRoleToEnabled() {
        SysRole role = SysRole.create(10L, "ADMIN", "Administrator", "System role", 20);

        assertEquals(10L, role.getCompanyId());
        assertEquals("ADMIN", role.getCode());
        assertEquals("Administrator", role.getName());
        assertEquals("System role", role.getRemark());
        assertEquals(20, role.getSort());
        assertEquals(Status.ENABLED, role.getStatus());
    }

    @Test
    void updateEditableFieldsKeepsIdentityAndStatus() {
        SysRole role = SysRole.create(10L, "OLD", "Old", null, 10).setId(7L);
        role.disable();

        role.updateEditableFields(20L, "NEW", "New", "Changed", 30);

        assertEquals(7L, role.getId());
        assertEquals(20L, role.getCompanyId());
        assertEquals("NEW", role.getCode());
        assertEquals("New", role.getName());
        assertEquals("Changed", role.getRemark());
        assertEquals(30, role.getSort());
        assertEquals(Status.DISABLED, role.getStatus());
    }

    @Test
    void businessFieldsDoNotExposePublicSetters() {
        Set<String> businessSetters = Set.of(
                "setCompanyId", "setCode", "setName", "setRemark", "setSort", "setStatus");

        boolean exposesBusinessSetter = Arrays.stream(SysRole.class.getDeclaredMethods())
                .filter(method -> businessSetters.contains(method.getName()))
                .anyMatch(this::isPublic);

        assertFalse(exposesBusinessSetter);
    }

    private boolean isPublic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }
}
