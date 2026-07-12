package com.manpowergroup.kintai.system.domain.entity.sys;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SysI18nTest {

    @Test
    void createCapturesTranslationIdentityAndContent() {
        SysI18n translation = SysI18n.create("ENUM", 10L, "ja", "Department");

        assertEquals("ENUM", translation.getRefType());
        assertEquals(10L, translation.getRefId());
        assertEquals("ja", translation.getLanguage());
        assertEquals("Department", translation.getContent());
    }

    @Test
    void changeContentKeepsTranslationIdentity() {
        SysI18n translation = SysI18n.create("ENUM", 10L, "ja", "Old").setId(7L);

        translation.changeContent("New");

        assertEquals(7L, translation.getId());
        assertEquals("ENUM", translation.getRefType());
        assertEquals(10L, translation.getRefId());
        assertEquals("ja", translation.getLanguage());
        assertEquals("New", translation.getContent());
    }

    @Test
    void businessFieldsDoNotExposePublicSetters() {
        Set<String> setters = Set.of("setRefType", "setRefId", "setLanguage", "setContent");

        boolean exposed = Arrays.stream(SysI18n.class.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));

        assertFalse(exposed);
    }
}
