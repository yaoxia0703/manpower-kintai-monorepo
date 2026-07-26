package com.manpowergroup.kintai.employee.domain.repository.emp;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;

public interface EmpEmployeeRepository {
    EmpEmployee getById(Long id);

    EmpEmployee findByEmail(String email);

    PageResult<EmpEmployee> findPageByCompany(long companyId, PageRequest request);

    PageResult<EmpEmployee> findPageByCompanyAndKeyword(long companyId,String keyword,PageRequest request);

    void save(EmpEmployee empEmployee);

    void updateById(EmpEmployee empEmployee);

    void deleteById(Long id);

    boolean existsByEmail(String email, Long excludeId);
}
