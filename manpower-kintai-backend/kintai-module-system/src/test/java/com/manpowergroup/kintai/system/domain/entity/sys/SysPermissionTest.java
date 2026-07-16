package com.manpowergroup.kintai.system.domain.entity.sys;

import com.manpowergroup.kintai.common.enums.PermissionHttpMethod;
import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SysPermissionTest {

    @Test
    void createDefaultsPermissionToEnabled() {
        SysPermission permission = SysPermission.create(
                10L, "employee:read", "Read employee", PermissionHttpMethod.GET, "/employees/**", null, 20);

        assertEquals(10L, permission.getMenuId());
        assertEquals("employee:read", permission.getCode());
        assertEquals("Read employee", permission.getName());
        assertEquals(PermissionHttpMethod.GET, permission.getMethod());
        assertEquals("/employees/**", permission.getPath());
        assertEquals(20, permission.getSort());
        assertEquals(Status.ENABLED, permission.getStatus());
    }

    @Test
    void updateEditableFieldsKeepsIdentityAndStatus() {
        SysPermission permission = SysPermission.create(
                10L, "old", "Old", PermissionHttpMethod.GET, "/old", null, 10).setId(7L);
        permission.disable();

        permission.updateEditableFields(
                20L, "new", "New", PermissionHttpMethod.POST, "/new", "Changed", 30);

        assertEquals(7L, permission.getId());
        assertEquals(20L, permission.getMenuId());
        assertEquals("new", permission.getCode());
        assertEquals("New", permission.getName());
        assertEquals(PermissionHttpMethod.POST, permission.getMethod());
        assertEquals("/new", permission.getPath());
        assertEquals("Changed", permission.getRemark());
        assertEquals(30, permission.getSort());
        assertEquals(Status.DISABLED, permission.getStatus());
    }

    @Test
    void businessFieldsDoNotExposePublicSetters() {
        Set<String> setters = Set.of(
                "setMenuId", "setCode", "setName", "setMethod", "setPath", "setRemark", "setSort", "setStatus");

        boolean exposed = Arrays.stream(SysPermission.class.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));

        assertFalse(exposed);
    }
}
