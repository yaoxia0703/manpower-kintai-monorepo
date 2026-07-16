package com.manpowergroup.kintai.attendance.domain.enums;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestTypeTest {

    @Test
    void definesCurrentAndLegacyRequestTypes() {
        Class<?> type = loadRequestType();

        assertNotNull(type, "RequestType enum must exist");
        assertTrue(type.isEnum());
        Set<String> names = Arrays.stream(type.getEnumConstants())
                .map(value -> ((Enum<?>) value).name())
                .collect(Collectors.toSet());
        assertEquals(Set.of(
                "PAID_LEAVE", "OVERTIME", "SUBSTITUTE",
                "BUSINESS_TRIP", "LEAVE_OF_ABSENCE"), names);
    }

    @Test
    void distinguishesCreatableTypesFromLegacyTypes() throws Exception {
        Class<?> type = requireRequestType();
        Method isCreatable = type.getMethod("isCreatable");

        assertTrue((boolean) isCreatable.invoke(enumValue(type, "PAID_LEAVE")));
        assertTrue((boolean) isCreatable.invoke(enumValue(type, "OVERTIME")));
        assertTrue((boolean) isCreatable.invoke(enumValue(type, "SUBSTITUTE")));
        assertFalse((boolean) isCreatable.invoke(enumValue(type, "BUSINESS_TRIP")));
        assertFalse((boolean) isCreatable.invoke(enumValue(type, "LEAVE_OF_ABSENCE")));
    }

    @Test
    void preservesTimesheetLockingBehaviorForLegacyLeave() throws Exception {
        Class<?> type = requireRequestType();
        Method isTimesheetLocking = type.getMethod("isTimesheetLocking");

        assertTrue((boolean) isTimesheetLocking.invoke(enumValue(type, "PAID_LEAVE")));
        assertTrue((boolean) isTimesheetLocking.invoke(enumValue(type, "SUBSTITUTE")));
        assertTrue((boolean) isTimesheetLocking.invoke(enumValue(type, "LEAVE_OF_ABSENCE")));
        assertFalse((boolean) isTimesheetLocking.invoke(enumValue(type, "OVERTIME")));
        assertFalse((boolean) isTimesheetLocking.invoke(enumValue(type, "BUSINESS_TRIP")));
    }

    private Class<?> requireRequestType() {
        Class<?> type = loadRequestType();
        assertNotNull(type, "RequestType enum must exist");
        return type;
    }

    private Class<?> loadRequestType() {
        try {
            return Class.forName("com.manpowergroup.kintai.attendance.domain.enums.RequestType");
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object enumValue(Class<?> type, String name) {
        return Enum.valueOf((Class<? extends Enum>) type, name);
    }
}
