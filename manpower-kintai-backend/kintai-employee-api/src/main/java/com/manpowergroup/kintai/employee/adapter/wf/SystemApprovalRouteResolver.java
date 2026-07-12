package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalRouteResolver;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.system.application.service.org.OrgGradeService;
import com.manpowergroup.kintai.system.application.service.org.OrgNodeService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeClosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SystemApprovalRouteResolver implements ApprovalRouteResolver {

    private static final String DIRECT_ONLY = "DIRECT_ONLY";
    private static final String REACH_GRADE = "REACH_GRADE";
    private static final String REACH_DEPARTMENT = "REACH_DEPARTMENT";

    private final EmpEmployeePositionService positionService;
    private final OrgNodeService nodeService;
    private final OrgGradeService gradeService;
    private final OrgNodeClosureRepository closureRepository;

    @Override
    public List<Long> resolveApprovers(Long employeeId, Long companyId, WfApprovalRule rule) {
        EmpEmployeePosition applicantPosition = positionService.getPrimaryByEmployee(employeeId);
        requireCompany(applicantPosition.getCompanyId(), companyId,
                "employee primary position belongs to another company");

        String stopCondition = rule == null ? DIRECT_ONLY : rule.getStopCondition();
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
            approvers.add(managerId);

            if (DIRECT_ONLY.equals(stopCondition)) {
                stopReached = true;
            } else if (REACH_DEPARTMENT.equals(stopCondition)) {
                stopReached = Objects.equals(node.getDeptFunction(), rule.getStopDeptFunc());
            } else if (REACH_GRADE.equals(stopCondition)) {
                stopReached = managerHasGrade(managerId, companyId, rule.getStopGradeLevel());
            } else {
                throw invalidRoute("unsupported approval stop condition: " + stopCondition);
            }
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

    private boolean managerHasGrade(Long managerId, Long companyId, String targetGradeLevel) {
        EmpEmployeePosition managerPosition = positionService.getPrimaryByEmployee(managerId);
        requireCompany(managerPosition.getCompanyId(), companyId,
                "approver primary position belongs to another company");
        OrgGrade grade = gradeService.getById(managerPosition.getGradeId());
        requireCompany(grade.getCompanyId(), companyId,
                "approver grade belongs to another company");
        return Objects.equals(grade.getGradeLevel(), targetGradeLevel);
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
