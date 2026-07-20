package com.manpowergroup.kintai.employee.application.service.emp;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;

import java.util.Optional;

public interface EmpEmployeeService {

    Optional<EmpEmployee> findById(Long id);

    Optional<EmpEmployee> findByEmail(String email);

    EmpEmployee getById(Long id);

    PageResult<EmpEmployee> pageByCompany(Long companyId, PageRequest request);

    PageResult<EmpEmployee> searchByName(Long companyId, String keyword, PageRequest request);

    EmpEmployee create(EmployeeCreateCommand command);

    EmpEmployee update(Long id, EmployeeUpdateCommand command);

    void enable(Long id);

    void disable(Long id);

    void remove(Long id);
}
