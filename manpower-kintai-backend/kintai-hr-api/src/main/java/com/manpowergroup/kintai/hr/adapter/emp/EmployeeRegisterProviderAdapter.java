package com.manpowergroup.kintai.hr.adapter.emp;

import com.manpowergroup.kintai.employee.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.application.service.hr.EmployeeOnboardingService;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterEntry;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterProvider;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterResult;
import com.manpowergroup.kintai.hr.assembler.emp.EmployeeRegisterAssembler;
import com.manpowergroup.kintai.system.application.service.sys.EmployeeRoleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeRegisterProviderAdapter implements EmployeeRegisterProvider {

    private final EmployeeRoleAssignmentService employeeRoleAssignmentService;
    private final EmpEmployeeService employeeService;
    private final EmpAccountService accountService;
    private final EmpEmployeePositionService positionService;

    @Override
    public EmployeeRegisterResult registerEmployee(EmployeeRegisterEntry entry) {

        var employee = employeeService.create(EmployeeRegisterAssembler.toEmployeeCreateCommand(entry));
        var account = accountService.create(EmployeeRegisterAssembler.toAccountCreateCommand(employee.getId(), entry));
        var position = positionService.create(EmployeeRegisterAssembler.toEmployeePositionCreateCommand(employee.getId(), entry));
        EmployeeRegisterAssembler.toEmployeeRoles(employee.getId(), entry).forEach(employeeRoleAssignmentService::assign);


        return new EmployeeRegisterResult(
            employee.getId(),
            account.getId(),
            position.getId(),
            employee.getEmployeeCode(),
            employee.getLastName() + " " + employee.getFirstName(),
            employee.getEmail()
        );

    }
}
