package com.manpowergroup.kintai.employee.application.service.impl.hr;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import com.manpowergroup.kintai.employee.application.assembler.hr.EmployeeOnboardingAssembler;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingOptionsResponse;
import com.manpowergroup.kintai.employee.application.dto.hr.request.EmployeeOnboardingRequest;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingResponse;
import com.manpowergroup.kintai.employee.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.application.service.hr.EmployeeOnboardingService;
import com.manpowergroup.kintai.system.application.service.sys.EmployeeRoleAssignmentService;
import com.manpowergroup.kintai.system.application.service.sys.RoleAccessService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgCompanyMapper;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgGradeMapper;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgNodeMapper;
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
    private final EmpEmployeeMapper employeeMapper;
    private final OrgCompanyMapper companyMapper;
    private final OrgNodeMapper nodeMapper;
    private final OrgGradeMapper gradeMapper;

    @Override
    public EmployeeOnboardingOptionsResponse options(Long operatorEmployeeId, Long companyId) {
        EmpEmployee operator = getOperator(operatorEmployeeId);
        boolean superAdmin = isSuperAdmin(operatorEmployeeId);
        Long targetCompanyId = resolveTargetCompanyId(operator, companyId, superAdmin);

        List<OrgCompany> companies = companyMapper.selectList(Wrappers.<OrgCompany>lambdaQuery()
                .eq(OrgCompany::getStatus, Status.ENABLED)
                .orderByAsc(OrgCompany::getLevel)
                .orderByAsc(OrgCompany::getSort));
        if (!superAdmin) {
            companies = companies.stream()
                    .filter(company -> Objects.equals(company.getId(), operator.getCompanyId()))
                    .toList();
        }

        List<OrgNode> nodes = nodeMapper.selectList(Wrappers.<OrgNode>lambdaQuery()
                .eq(OrgNode::getCompanyId, targetCompanyId)
                .eq(OrgNode::getStatus, Status.ENABLED)
                .orderByAsc(OrgNode::getLevel)
                .orderByAsc(OrgNode::getSort));
        List<OrgGrade> grades = gradeMapper.selectList(Wrappers.<OrgGrade>lambdaQuery()
                .eq(OrgGrade::getCompanyId, targetCompanyId)
                .eq(OrgGrade::getStatus, Status.ENABLED)
                .orderByAsc(OrgGrade::getSort));
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
    public EmployeeOnboardingResponse onboard(EmployeeOnboardingRequest request, Long operatorEmployeeId) {
        EmpEmployee operator = getOperator(operatorEmployeeId);
        boolean superAdmin = isSuperAdmin(operatorEmployeeId);
        validateTargetCompany(operator, request.getCompanyId(), superAdmin);
        validateOnboardingReferences(request);

        if (!roleAccessService.areAllEnabledForCompany(request.getCompanyId(), request.getRoleIds())) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "roleIds contain invalid role");
        }

        EmpEmployee employee = employeeService.create(
                EmployeeOnboardingAssembler.toEmployee(request, operatorEmployeeId));

        EmpAccount account = accountService.create(
                EmployeeOnboardingAssembler.toAccount(request, employee.getId(), operatorEmployeeId));

        EmpEmployeePosition position = positionService.create(
                EmployeeOnboardingAssembler.toPosition(request, employee.getId(), operatorEmployeeId));

        EmployeeOnboardingAssembler.toEmployeeRoles(request, employee.getId(), operatorEmployeeId)
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
        EmpEmployee operator = employeeMapper.selectById(operatorEmployeeId);
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

        Long companyCount = companyMapper.selectCount(Wrappers.<OrgCompany>lambdaQuery()
                .eq(OrgCompany::getId, targetCompanyId)
                .eq(OrgCompany::getStatus, Status.ENABLED));
        if (companyCount != 1) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "companyId is invalid");
        }
    }

    private void validateOnboardingReferences(EmployeeOnboardingRequest request) {
        Long nodeCount = nodeMapper.selectCount(Wrappers.<OrgNode>lambdaQuery()
                .eq(OrgNode::getId, request.getNodeId())
                .eq(OrgNode::getCompanyId, request.getCompanyId())
                .eq(OrgNode::getStatus, Status.ENABLED));
        if (nodeCount != 1) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "nodeId is invalid for company");
        }

        Long gradeCount = gradeMapper.selectCount(Wrappers.<OrgGrade>lambdaQuery()
                .eq(OrgGrade::getId, request.getGradeId())
                .eq(OrgGrade::getCompanyId, request.getCompanyId())
                .eq(OrgGrade::getStatus, Status.ENABLED));
        if (gradeCount != 1) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "gradeId is invalid for company");
        }
    }
}
