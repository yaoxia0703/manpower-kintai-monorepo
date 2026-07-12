package com.manpowergroup.kintai.system.domain.entity.sys;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SysEnumTypeTest {

    @Test
    void createDefaultsStatusToEnabled() {
        SysEnumType enumType = SysEnumType.create("ORG_TYPE", "Organization type", null, 10, null);

        assertEquals("ORG_TYPE", enumType.getCode());
        assertEquals("Organization type", enumType.getName());
        assertEquals(10, enumType.getSort());
        assertEquals(Status.ENABLED, enumType.getStatus());
    }

    @Test
    void updateEditableFieldsKeepsIdentityAndStatus() {
        SysEnumType enumType = SysEnumType.create("OLD", "Old", null, 10, Status.DISABLED).setId(7L);

        enumType.updateEditableFields("NEW", "New", "Changed", 20);

        assertEquals(7L, enumType.getId());
        assertEquals("NEW", enumType.getCode());
        assertEquals("New", enumType.getName());
        assertEquals("Changed", enumType.getRemark());
        assertEquals(20, enumType.getSort());
        assertEquals(Status.DISABLED, enumType.getStatus());
    }

    @Test
    void businessFieldsDoNotExposePublicSetters() {
        Set<String> setters = Set.of("setCode", "setName", "setRemark", "setSort", "setStatus");

        boolean exposed = Arrays.stream(SysEnumType.class.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));

        assertFalse(exposed);
    }
}
