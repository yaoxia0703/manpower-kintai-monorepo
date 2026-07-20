package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalRouteResolver;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.employee.application.service.org.OrgGradeService;
import com.manpowergroup.kintai.employee.application.service.org.OrgNodeService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgNodeClosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EmployeeApprovalRouteResolver implements ApprovalRouteResolver {

    private static final Set<String> APPROVAL_GRADE_LEVELS = Set.of("L1", "L2", "L3");

    private final EmpEmployeePositionService positionService;
    private final OrgNodeService nodeService;
    private final OrgGradeService gradeService;
    private final OrgNodeClosureRepository closureRepository;

    @Override
    public List<Long> resolveApprovers(Long employeeId, Long companyId, WfApprovalRule rule) {
        EmpEmployeePosition applicantPosition = positionService.getPrimaryByEmployee(employeeId);
        requireCompany(applicantPosition.getCompanyId(), companyId,
                "employee primary position belongs to another company");

        ApprovalStopCondition stopCondition = rule == null
                ? ApprovalStopCondition.DIRECT_ONLY
                : rule.getStopCondition();
        Set<Long> approvers = new LinkedHashSet<>();
        boolean stopReached = false;

        for (var closure : closureRepository.findAncestors(applicantPosition.getNodeId())) {
            OrgNode node = nodeService.getById(closure.getAncestorId());
            requireCompany(node.getCompanyId(), companyId,
                    "employee organization hierarchy crosses company boundary");
            Long managerId = node.getManagerId();
            if (managerId == null || Objects.equals(managerId, employeeId)) {
                continue;
            }
            String managerGradeLevel = managerGradeLevel(managerId, companyId);
            if (!APPROVAL_GRADE_LEVELS.contains(managerGradeLevel)) {
                continue;
            }
            approvers.add(managerId);

            stopReached = switch (stopCondition) {
                case DIRECT_ONLY -> true;
                case REACH_DEPARTMENT -> Objects.equals(node.getDeptFunction(), rule.getStopDeptFunc());
                case REACH_GRADE -> Objects.equals(managerGradeLevel, rule.getStopGradeLevel());
            };
            if (stopReached) {
                break;
            }
        }

        if (approvers.isEmpty()) {
            throw invalidRoute("attendance request has no approver in organization hierarchy");
        }
        if (!stopReached) {
            throw invalidRoute("configured approval stop condition cannot be reached");
        }
        return List.copyOf(approvers);
    }

    private String managerGradeLevel(Long managerId, Long companyId) {
        EmpEmployeePosition managerPosition = positionService.getPrimaryByEmployee(managerId);
        requireCompany(managerPosition.getCompanyId(), companyId,
                "approver primary position belongs to another company");
        OrgGrade grade = gradeService.getById(managerPosition.getGradeId());
        requireCompany(grade.getCompanyId(), companyId,
                "approver grade belongs to another company");
        return grade.getGradeLevel();
    }

    private void requireCompany(Long actualCompanyId, Long expectedCompanyId, String detail) {
        if (!Objects.equals(actualCompanyId, expectedCompanyId)) {
            throw invalidRoute(detail);
        }
    }

    private BizException invalidRoute(String detail) {
        return BizException.withDetail(ErrorCode.CONFLICT, detail);
    }
}
