package com.manpowergroup.kintai.system.domain.entity.emp;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

class EmployeeEntityEncapsulationTest {

    @Test
    void employeeDoesNotExposeBusinessSetters() {
        assertNoPublicSetters(EmpEmployee.class, Set.of(
                "setCompanyId", "setEmployeeCode", "setLastName", "setFirstName",
                "setLastNameKana", "setFirstNameKana", "setEmail", "setPhone",
                "setGender", "setBirthDate", "setHireDate", "setLeaveDate", "setStatus"));
    }

    @Test
    void positionDoesNotExposeBusinessSetters() {
        assertNoPublicSetters(EmpEmployeePosition.class, Set.of(
                "setEmployeeId", "setCompanyId", "setNodeId", "setGradeId", "setIsPrimary",
                "setStartDate", "setEndDate", "setStatus"));
    }

    private void assertNoPublicSetters(Class<?> type, Set<String> setters) {
        boolean exposed = Arrays.stream(type.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));
        assertFalse(exposed);
    }
}
