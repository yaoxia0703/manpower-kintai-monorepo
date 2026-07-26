package com.manpowergroup.kintai.employee.domain.repository.emp;

import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;

import java.util.List;

public interface EmpEmployeePositionRepository {

    EmpEmployeePosition getById(long id);

    List<EmpEmployeePosition> getByEmployeeIdAndStartDateAndEndDate(long employeeId);

    List<EmpEmployeePosition> getByEmployeeId(long employeeId);

    EmpEmployeePosition getPrimaryByEmployee(long employeeId);

    void save(EmpEmployeePosition empEmployeePosition);

    void updateById(EmpEmployeePosition empEmployeePosition);
    void deleteById(long id);
}
