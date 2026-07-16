package com.manpowergroup.kintai.attendance.domain.enums;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApprovalStopConditionTest {

    @Test
    void definesSupportedApprovalStopConditions() {
        Class<?> type = loadType();

        assertNotNull(type, "ApprovalStopCondition enum must exist");
        assertTrue(type.isEnum());
        Set<String> names = Arrays.stream(type.getEnumConstants())
                .map(value -> ((Enum<?>) value).name())
                .collect(Collectors.toSet());
        assertEquals(Set.of("DIRECT_ONLY", "REACH_GRADE", "REACH_DEPARTMENT"), names);
    }

    @Test
    void preservesDatabaseAndJsonCodes() {
        assertEquals(ApprovalStopCondition.REACH_GRADE,
                ApprovalStopCondition.fromJson("REACH_GRADE"));
        assertEquals("REACH_GRADE", ApprovalStopCondition.REACH_GRADE.toJson());
    }

    private Class<?> loadType() {
        try {
            return Class.forName("com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition");
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }
}
