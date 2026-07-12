package com.manpowergroup.kintai.system.domain.entity.sys;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SysEnumValueTest {

    @Test
    void createDefaultsStatusToEnabled() {
        SysEnumValue enumValue = SysEnumValue.create("ORG_TYPE", "DEPARTMENT", 10, null);

        assertEquals("ORG_TYPE", enumValue.getEnumTypeCode());
        assertEquals("DEPARTMENT", enumValue.getCode());
        assertEquals(10, enumValue.getSort());
        assertEquals(Status.ENABLED, enumValue.getStatus());
    }

    @Test
    void updateEditableFieldsMovesValueToRequestedEnumTypeAndKeepsStatus() {
        SysEnumValue enumValue = SysEnumValue.create("OLD_TYPE", "OLD", 10, Status.DISABLED).setId(7L);

        enumValue.updateEditableFields("NEW_TYPE", "NEW", 20);

        assertEquals(7L, enumValue.getId());
        assertEquals("NEW_TYPE", enumValue.getEnumTypeCode());
        assertEquals("NEW", enumValue.getCode());
        assertEquals(20, enumValue.getSort());
        assertEquals(Status.DISABLED, enumValue.getStatus());
    }

    @Test
    void businessFieldsDoNotExposePublicSetters() {
        Set<String> setters = Set.of("setEnumTypeCode", "setCode", "setSort", "setStatus");

        boolean exposed = Arrays.stream(SysEnumValue.class.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));

        assertFalse(exposed);
    }
}
