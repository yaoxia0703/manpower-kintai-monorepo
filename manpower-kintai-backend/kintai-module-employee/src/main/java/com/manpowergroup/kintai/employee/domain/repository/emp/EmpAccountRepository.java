package com.manpowergroup.kintai.employee.domain.repository.emp;

import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;

public interface EmpAccountRepository {
    EmpAccount getById(Long id);

    EmpAccount getByEmployeeId(Long employeeId);

    long countByUsername(String username);

    boolean existsByUsernameExcludingId(String username, Long excludeId);

    void save(EmpAccount empAccount);

    void updateById(EmpAccount empAccount);

    void deleteById(Long id);
}
