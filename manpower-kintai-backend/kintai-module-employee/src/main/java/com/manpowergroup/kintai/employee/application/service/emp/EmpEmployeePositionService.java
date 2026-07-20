package com.manpowergroup.kintai.employee.application.service.emp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;

import java.util.List;

public interface EmpEmployeePositionService extends IService<EmpEmployeePosition> {

    EmpEmployeePosition getById(Long id);

    List<EmpEmployeePosition> listActiveByEmployee(Long employeeId);

    List<EmpEmployeePosition> listAllByEmployee(Long employeeId);

    EmpEmployeePosition getPrimaryByEmployee(Long employeeId);

    EmpEmployeePosition create(EmployeePositionCreateCommand command);

    EmpEmployeePosition update(Long id, EmployeePositionUpdateCommand command);

    void terminate(Long id);

    void remove(Long id);
}
