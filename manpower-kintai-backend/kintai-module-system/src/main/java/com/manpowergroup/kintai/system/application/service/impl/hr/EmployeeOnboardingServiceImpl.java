package com.manpowergroup.kintai.system.application.service.impl.hr;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import com.manpowergroup.kintai.system.application.assembler.hr.EmployeeOnboardingAssembler;
import com.manpowergroup.kintai.system.application.dto.hr.response.EmployeeOnboardingOptionsResponse;
import com.manpowergroup.kintai.system.application.dto.hr.request.EmployeeOnboardingRequest;
import com.manpowergroup.kintai.system.application.dto.hr.response.EmployeeOnboardingResponse;
import com.manpowergroup.kintai.system.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.system.application.service.hr.EmployeeOnboardingService;
import com.manpowergroup.kintai.system.application.service.sys.EmployeeRoleAssignmentService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.system.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgCompanyMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgGradeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
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
    private final EmpEmployeeMapper employeeMapper;
    private final OrgCompanyMapper companyMapper;
    private final OrgNodeMapper nodeMapper;
    private final OrgGradeMapper gradeMapper;
    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRoleMapper roleMapper;

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
        List<SysRole> roles = roleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getCompanyId, targetCompanyId)
                .eq(SysRole::getStatus, Status.ENABLED)
                .orderByAsc(SysRole::getSort));

        return EmployeeOnboardingOptionsResponse.builder()
                .selectedCompanyId(targetCompanyId)
                .companies(companies.stream().map(EmployeeOnboardingOptionsResponse.CompanyOption::from).toList())
                .nodes(nodes.stream().map(EmployeeOnboardingOptionsResponse.NodeOption::from).toList())
                .grades(grades.stream().map(EmployeeOnboardingOptionsResponse.GradeOption::from).toList())
                .roles(roles.stream().map(EmployeeOnboardingOptionsResponse.RoleOption::from).toList())
                .build();
    }

    @Override
    @Transactional
    public EmployeeOnboardingResponse onboard(EmployeeOnboardingRequest request, Long operatorEmployeeId) {
        EmpEmployee operator = getOperator(operatorEmployeeId);
        boolean superAdmin = isSuperAdmin(operatorEmployeeId);
        validateTargetCompany(operator, request.getCompanyId(), superAdmin);
        validateOnboardingReferences(request);

        long validRoleCount = roleMapper.selectCount(Wrappers.<SysRole>lambdaQuery()
                .in(SysRole::getId, request.getRoleIds())
                .eq(SysRole::getCompanyId, request.getCompanyId())
                .eq(SysRole::getStatus, Status.ENABLED));
        if (validRoleCount != request.getRoleIds().size()) {
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
        LocalDate today = LocalDate.now();
        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId))
                .stream()
                .filter(assignment -> assignment.isEffectiveOn(today))
                .map(SysEmployeeRole::getRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (roleIds.isEmpty()) return false;

        return roleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                        .in(SysRole::getId, roleIds)
                        .eq(SysRole::getStatus, Status.ENABLED))
                .stream()
                .anyMatch(role -> SUPER_ADMIN_ROLE.equals(role.getCode()));
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
