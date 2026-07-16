package com.manpowergroup.kintai.framework.security.authority;

import com.manpowergroup.kintai.common.enums.PermissionHttpMethod;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermissionHttpMethodTest {

    @Test
    void definesStandardPermissionHttpMethods() {
        Class<?> type = loadType();

        assertNotNull(type, "PermissionHttpMethod enum must exist");
        assertTrue(type.isEnum());
        Set<String> names = Arrays.stream(type.getEnumConstants())
                .map(value -> ((Enum<?>) value).name())
                .collect(Collectors.toSet());
        assertEquals(Set.of(
                "GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE", "CONNECT"), names);
    }

    @Test
    void parsesCaseInsensitiveJsonAndPreservesWireValue() {
        assertEquals(PermissionHttpMethod.PATCH, PermissionHttpMethod.fromJson("patch"));
        assertEquals("PATCH", PermissionHttpMethod.PATCH.toJson());
    }

    private Class<?> loadType() {
        try {
            return Class.forName("com.manpowergroup.kintai.common.enums.PermissionHttpMethod");
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }
}
