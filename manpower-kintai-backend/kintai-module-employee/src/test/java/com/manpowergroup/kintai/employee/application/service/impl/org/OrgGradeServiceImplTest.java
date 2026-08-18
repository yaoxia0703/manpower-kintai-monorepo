package com.manpowergroup.kintai.employee.application.service.impl.org;

import com.manpowergroup.kintai.employee.domain.repository.org.OrgGradeRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrgGradeServiceImplTest {

    @Test
    void gradeBelongsToCompanyReturnsTrueWhenAssociationExists() {
        OrgGradeRepository repository = mock(OrgGradeRepository.class);
        OrgGradeServiceImpl service = new OrgGradeServiceImpl(repository);
        when(repository.selectCountByCompanyAndGrade(10L, 30L)).thenReturn(1L);

        assertTrue(service.gradeBelongsToCompany(30L, 10L));
    }

    @Test
    void gradeBelongsToCompanyReturnsFalseWhenAssociationDoesNotExist() {
        OrgGradeRepository repository = mock(OrgGradeRepository.class);
        OrgGradeServiceImpl service = new OrgGradeServiceImpl(repository);
        when(repository.selectCountByCompanyAndGrade(10L, 30L)).thenReturn(0L);

        assertFalse(service.gradeBelongsToCompany(30L, 10L));
    }
}
