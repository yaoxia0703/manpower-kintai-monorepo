package com.manpowergroup.kintai.employee.domain.entity.org;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrgGradeTest {

    @Test
    void createDefaultsStatusToEnabledWhenStatusIsNotSpecified() {
        OrgGrade grade = OrgGrade.create(1L, "Manager", "MGR", "L4", 10, null);

        assertEquals(1L, grade.getCompanyId());
        assertEquals("Manager", grade.getName());
        assertEquals("MGR", grade.getCode());
        assertEquals("L4", grade.getGradeLevel());
        assertEquals(10, grade.getSort());
        assertEquals(Status.ENABLED, grade.getStatus());
    }

    @Test
    void updateEditableFieldsKeepsIdentityCompanyAndStatus() {
        OrgGrade grade = OrgGrade.create(1L, "Old", "OLD", "L1", 10, Status.DISABLED)
                .setId(99L);

        grade.updateEditableFields("New", "NEW", "L2", 20);

        assertEquals(99L, grade.getId());
        assertEquals(1L, grade.getCompanyId());
        assertEquals("New", grade.getName());
        assertEquals("NEW", grade.getCode());
        assertEquals("L2", grade.getGradeLevel());
        assertEquals(20, grade.getSort());
        assertEquals(Status.DISABLED, grade.getStatus());
    }
}
