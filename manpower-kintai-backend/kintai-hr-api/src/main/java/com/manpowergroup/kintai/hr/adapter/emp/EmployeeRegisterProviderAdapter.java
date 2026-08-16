package com.manpowergroup.kintai.hr.adapter.emp;

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

    private final EmployeeOnboardingService employeeOnboardingService;
    private final EmployeeRoleAssignmentService employeeRoleAssignmentService;
    private final EmpEmployeeService employeeService;

    @Override
    public EmployeeRegisterResult registerEmployee(EmployeeRegisterEntry entry, Long employeeId) {

        return EmployeeRegisterAssembler.toResult(employeeOnboardingService.onboard(EmployeeRegisterAssembler.toEmployeeOnboardingCommand(entry), employeeId));
    }
}
