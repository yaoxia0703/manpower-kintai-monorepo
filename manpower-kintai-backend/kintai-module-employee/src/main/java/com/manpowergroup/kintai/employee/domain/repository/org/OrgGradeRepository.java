package com.manpowergroup.kintai.employee.domain.repository.org;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;

import java.util.List;

public interface OrgGradeRepository {

    List<OrgGrade> findListByCompany(Long companyId);

    Long selectCountByCompanyAndGrade(Long companyId, Long gradeId);

    OrgGrade getById(Long id);

    PageResult<OrgGrade> findPageByCompany(Long companyId, int page, int size);

    List<OrgGrade> findByCompany(Long companyId);

    List<OrgGrade> findByGradeLevel(String gradeLevel);

    boolean selectCountByCodeAndCompany(String code, Long companyId);

    void save(OrgGrade grade);

    void updateById(OrgGrade grade);

    void deleteById(Long id);

    boolean existsByCompanyAndCodeExcludingId(Long id, Long companyId, String code);

}
