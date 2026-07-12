package com.manpowergroup.kintai.system.domain.entity.sys;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SysMenuTest {

    @Test
    void createDefaultsMenuToVisibleAndEnabled() {
        SysMenu menu = SysMenu.create(
                null, "Employees", "employees", "/employees", "EmployeePage", "users", 2, 20, null);

        assertEquals("Employees", menu.getName());
        assertEquals("employees", menu.getCode());
        assertEquals(1, menu.getVisible());
        assertEquals(Status.ENABLED, menu.getStatus());
    }

    @Test
    void updateEditableFieldsKeepsIdentityAndStatus() {
        SysMenu menu = SysMenu.create(
                null, "Old", "old", "/old", "OldPage", null, 2, 10, 1).setId(7L);
        menu.disable();

        menu.updateEditableFields(5L, "New", "new", "/new", "NewPage", "menu", 3, 30, 0);

        assertEquals(7L, menu.getId());
        assertEquals(5L, menu.getParentId());
        assertEquals("New", menu.getName());
        assertEquals("new", menu.getCode());
        assertEquals("/new", menu.getPath());
        assertEquals("NewPage", menu.getComponent());
        assertEquals("menu", menu.getIcon());
        assertEquals(3, menu.getType());
        assertEquals(30, menu.getSort());
        assertEquals(0, menu.getVisible());
        assertEquals(Status.DISABLED, menu.getStatus());
    }

    @Test
    void businessFieldsDoNotExposePublicSetters() {
        Set<String> setters = Set.of(
                "setParentId", "setName", "setCode", "setPath", "setComponent", "setIcon",
                "setType", "setSort", "setVisible", "setStatus");

        boolean exposed = Arrays.stream(SysMenu.class.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));

        assertFalse(exposed);
    }
}
