package com.manpowergroup.kintai.system.domain.entity.org;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

class OrganizationEntityEncapsulationTest {

    @Test
    void companyDoesNotExposeBusinessSetters() {
        assertNoPublicSetters(OrgCompany.class, Set.of(
                "setParentId", "setName", "setCompanyCode", "setLevel", "setSort", "setStatus"));
    }

    @Test
    void gradeDoesNotExposeBusinessSetters() {
        assertNoPublicSetters(OrgGrade.class, Set.of(
                "setCompanyId", "setName", "setCode", "setGradeLevel", "setSort", "setStatus"));
    }

    @Test
    void nodeDoesNotExposeBusinessSetters() {
        assertNoPublicSetters(OrgNode.class, Set.of(
                "setCompanyId", "setParentId", "setManagerId", "setName", "setTypeCode",
                "setDeptFunction", "setCode", "setLevel", "setSort", "setStatus"));
    }

    private void assertNoPublicSetters(Class<?> type, Set<String> setters) {
        boolean exposed = Arrays.stream(type.getDeclaredMethods())
                .filter(method -> setters.contains(method.getName()))
                .anyMatch(method -> Modifier.isPublic(method.getModifiers()));
        assertFalse(exposed);
    }
}
