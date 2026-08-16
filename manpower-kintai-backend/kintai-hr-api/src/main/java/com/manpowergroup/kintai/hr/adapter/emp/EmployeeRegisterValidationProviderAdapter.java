package com.manpowergroup.kintai.hr.adapter.emp;

import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.application.service.org.OrgCompanyService;
import com.manpowergroup.kintai.employee.application.service.org.OrgGradeService;
import com.manpowergroup.kintai.employee.application.service.org.OrgNodeService;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterValidationPort;
import com.manpowergroup.kintai.hr.assembler.emp.EmployeeRegisterAssembler;
import com.manpowergroup.kintai.system.application.service.sys.RoleAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmployeeRegisterValidationProviderAdapter implements EmployeeRegisterValidationPort {

    private static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";
    private final EmpEmployeeService employeeService;
    private final RoleAccessService roleAccessService;
    private final OrgCompanyService orgCompanyService;
    private final OrgGradeService orgGradeService;
    private final OrgNodeService orgNodeService;

    @Override
    public Optional<OperatorInfo> getOperator(Long operatorEmployeeId) {
        return employeeService.findById(operatorEmployeeId)
            .map(EmployeeRegisterAssembler::toOperatorInfo);
    }

    @Override
    public boolean companyExists(Long companyId) {
        return orgCompanyService.companyExists(companyId);
    }

    @Override
    public boolean nodeBelongsToCompany(Long nodeId, Long companyId) {
        return orgNodeService.nodeBelongsToCompany(nodeId, companyId);
    }

    @Override
    public boolean gradeBelongsToCompany(Long gradeId, Long companyId) {
        return orgGradeService.gradeBelongsToCompany(gradeId, companyId);
    }

    @Override
    public boolean hasSuperAdminRole(Long employeeId) {
        return roleAccessService.employeeHasActiveRole(employeeId, SUPER_ADMIN_ROLE, LocalDate.now());
    }

    @Override
    public boolean rolesAreEnabledForCompany(Long companyId, List<Long> roleIds) {
        return roleAccessService.areAllEnabledForCompany(companyId, roleIds);
    }
}
