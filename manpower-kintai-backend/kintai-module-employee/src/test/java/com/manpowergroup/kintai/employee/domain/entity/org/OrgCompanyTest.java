package com.manpowergroup.kintai.employee.domain.entity.org;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrgCompanyTest {

    @Test
    void createDefaultsStatusToEnabledWhenStatusIsNotSpecified() {
        OrgCompany company = OrgCompany.create(1L, "Manpower", "MP", 2, 10, null);

        assertEquals(1L, company.getParentId());
        assertEquals("Manpower", company.getName());
        assertEquals("MP", company.getCompanyCode());
        assertEquals(2, company.getLevel());
        assertEquals(10, company.getSort());
        assertEquals(Status.ENABLED, company.getStatus());
    }

    @Test
    void updateEditableFieldsKeepsIdentityAndStatus() {
        OrgCompany company = OrgCompany.create(null, "Old", "OLD", 1, 10, Status.DISABLED)
                .setId(99L);

        company.updateEditableFields(1L, "New", "NEW", 2, 20);

        assertEquals(99L, company.getId());
        assertEquals(1L, company.getParentId());
        assertEquals("New", company.getName());
        assertEquals("NEW", company.getCompanyCode());
        assertEquals(2, company.getLevel());
        assertEquals(20, company.getSort());
        assertEquals(Status.DISABLED, company.getStatus());
    }
}
