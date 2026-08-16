package com.manpowergroup.kintai.employee.application.service.impl.hr;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import com.manpowergroup.kintai.employee.application.assembler.hr.EmployeeOnboardingAssembler;
import com.manpowergroup.kintai.employee.application.command.hr.EmployeeOnboardingCommand;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingOptionsResponse;
import com.manpowergroup.kintai.employee.application.dto.hr.request.EmployeeOnboardingRequest;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingResponse;
import com.manpowergroup.kintai.employee.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.application.service.hr.EmployeeOnboardingService;
import com.manpowergroup.kintai.employee.domain.repository.emp.EmpEmployeeRepository;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgCompanyRepository;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgGradeRepository;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgNodeRepository;
import com.manpowergroup.kintai.system.application.service.sys.EmployeeRoleAssignmentService;
import com.manpowergroup.kintai.system.application.service.sys.RoleAccessService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeOnboardingServiceImpl implements EmployeeOnboardingService {

    private static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";

    private final EmpEmployeeService employeeService;
    private final EmpAccountService accountService;
    private final EmpEmployeePositionService positionService;
    private final EmployeeRoleAssignmentService employeeRoleAssignmentService;
    private final RoleAccessService roleAccessService;
    private final EmpEmployeeRepository empEmployeeRepository;
    private final OrgCompanyRepository orgCompanyRepository;
    private final OrgNodeRepository orgNodeRepository;
    private final OrgGradeRepository orgGradeRepository;

    @Override
    public EmployeeOnboardingOptionsResponse options(Long operatorEmployeeId, Long companyId) {
        EmpEmployee operator = getOperator(operatorEmployeeId);
        boolean superAdmin = isSuperAdmin(operatorEmployeeId);
        Long targetCompanyId = resolveTargetCompanyId(operator, companyId, superAdmin);

        List<OrgCompany> companies = orgCompanyRepository.findList();
        if (!superAdmin) {
            companies = companies.stream()
                .filter(company -> Objects.equals(company.getId(), operator.getCompanyId()))
                .toList();
        }

        List<OrgNode> nodes = orgNodeRepository.findListByCompany(targetCompanyId);
        List<OrgGrade> grades = orgGradeRepository.findListByCompany(targetCompanyId);

        return EmployeeOnboardingOptionsResponse.builder()
            .selectedCompanyId(targetCompanyId)
            .companies(companies.stream().map(EmployeeOnboardingOptionsResponse.CompanyOption::from).toList())
            .nodes(nodes.stream().map(EmployeeOnboardingOptionsResponse.NodeOption::from).toList())
            .grades(grades.stream().map(EmployeeOnboardingOptionsResponse.GradeOption::from).toList())
            .roles(roleAccessService.listEnabledByCompany(targetCompanyId).stream()
                .map(EmployeeOnboardingOptionsResponse.RoleOption::from)
                .toList())
            .build();
    }

    @Override
    @Transactional
    public EmployeeOnboardingResponse onboard(EmployeeOnboardingCommand command, Long operatorEmployeeId) {
        EmpEmployee operator = getOperator(operatorEmployeeId);
        boolean superAdmin = isSuperAdmin(operatorEmployeeId);
        validateTargetCompany(operator, command.companyId(), superAdmin);
        validateOnboardingReferences(command);

        if (!roleAccessService.areAllEnabledForCompany(command.companyId(), command.roleIds())) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "roleIds contain invalid role");
        }

        EmpEmployee employee = employeeService.create(
            EmployeeOnboardingAssembler.toEmployee(command, operatorEmployeeId));

        EmpAccount account = accountService.create(
            EmployeeOnboardingAssembler.toAccount(command, employee.getId(), operatorEmployeeId));

        EmpEmployeePosition position = positionService.create(
            EmployeeOnboardingAssembler.toPosition(command, employee.getId(), operatorEmployeeId));

        EmployeeOnboardingAssembler.toEmployeeRoles(command, employee.getId(), operatorEmployeeId)
            .forEach(employeeRoleAssignmentService::assign);

        return EmployeeOnboardingResponse.builder()
            .employeeId(employee.getId())
            .accountId(account.getId())
            .positionId(position.getId())
            .employeeCode(employee.getEmployeeCode())
            .displayName(employee.getLastName() + " " + employee.getFirstName())
            .email(employee.getEmail())
            .build();
    }

    private EmpEmployee getOperator(Long operatorEmployeeId) {
        EmpEmployee operator = empEmployeeRepository.getById(operatorEmployeeId);
        if (operator == null || operator.getStatus() != Status.ENABLED) {
            throw new BizException(ErrorCode.UNAUTHORIZED);
        }
        return operator;
    }

    private boolean isSuperAdmin(Long employeeId) {
        return roleAccessService.employeeHasActiveRole(employeeId, SUPER_ADMIN_ROLE, LocalDate.now());
    }

    private Long resolveTargetCompanyId(EmpEmployee operator, Long requestedCompanyId, boolean superAdmin) {
        Long targetCompanyId = requestedCompanyId != null ? requestedCompanyId : operator.getCompanyId();
        validateTargetCompany(operator, targetCompanyId, superAdmin);
        return targetCompanyId;
    }

    private void validateTargetCompany(EmpEmployee operator, Long targetCompanyId, boolean superAdmin) {
        if (targetCompanyId == null) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "companyId is required");
        }
        if (!superAdmin && !Objects.equals(operator.getCompanyId(), targetCompanyId)) {
            throw BizException.withDetail(ErrorCode.FORBIDDEN, "HR can only onboard employees in own company");
        }

        Long companyCount = orgCompanyRepository.selectCount(targetCompanyId);
        if (companyCount != 1) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "companyId is invalid");
        }
    }

    private void validateOnboardingReferences(EmployeeOnboardingCommand command) {
        Long nodeCount = orgNodeRepository.selectCountByNodeAndComoany(command.nodeId(), command.companyId());
        if (nodeCount != 1) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "nodeId is invalid for company");
        }

        Long gradeCount = orgGradeRepository.selectCountByCompanyAndGrade(command.companyId(), command.gradeId());
        if (gradeCount != 1) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "gradeId is invalid for company");
        }
    }
}
