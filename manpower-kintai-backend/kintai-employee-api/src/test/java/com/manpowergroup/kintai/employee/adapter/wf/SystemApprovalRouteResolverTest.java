package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.system.application.service.org.OrgGradeService;
import com.manpowergroup.kintai.system.application.service.org.OrgNodeService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeClosureRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class SystemApprovalRouteResolverTest {

    @Test
    void directOnlyUsesNearestManagerAndSkipsApplicant() {
        Fixture fixture = fixture();
        fixture.nodes(1L, 30L);

        assertEquals(List.of(30L), fixture.resolver.resolveApprovers(1L, 10L, null));
    }

    @Test
    void reachDepartmentIncludesManagersThroughMatchingDepartment() {
        Fixture fixture = fixture();
        fixture.nodes(20L, 30L);
        when(fixture.nodeService.getById(100L)).thenReturn(node(100L, 20L, "SALES"));
        when(fixture.nodeService.getById(200L)).thenReturn(node(200L, 30L, "HR"));
        WfApprovalRule rule = WfApprovalRule.create(
                10L, "PAID_LEAVE", "REACH_DEPARTMENT",
                null, "HR", null, 1, Status.ENABLED);

        assertEquals(List.of(20L, 30L),
                fixture.resolver.resolveApprovers(1L, 10L, rule));
    }

    @Test
    void reachGradeIncludesManagersThroughMatchingGrade() {
        Fixture fixture = fixture();
        fixture.nodes(20L, 30L);
        when(fixture.positionService.getPrimaryByEmployee(20L)).thenReturn(position(20L, 2L));
        when(fixture.positionService.getPrimaryByEmployee(30L)).thenReturn(position(30L, 3L));
        when(fixture.gradeService.getById(2L)).thenReturn(grade(2L, "L3"));
        when(fixture.gradeService.getById(3L)).thenReturn(grade(3L, "L4"));
        WfApprovalRule rule = WfApprovalRule.create(
                10L, "PAID_LEAVE", "REACH_GRADE",
                "L4", null, null, 1, Status.ENABLED);

        assertEquals(List.of(20L, 30L),
                fixture.resolver.resolveApprovers(1L, 10L, rule));
    }

    @Test
    void rejectsRouteWhenConfiguredStopConditionCannotBeReached() {
        Fixture fixture = fixture();
        fixture.nodes(20L, 30L);
        WfApprovalRule rule = WfApprovalRule.create(
                10L, "PAID_LEAVE", "REACH_DEPARTMENT",
                null, "FINANCE", null, 1, Status.ENABLED);

        assertThrows(BizException.class,
                () -> fixture.resolver.resolveApprovers(1L, 10L, rule));
    }

    @Test
    void rejectsOrganizationPositionFromAnotherCompany() {
        Fixture fixture = fixture();
        when(fixture.positionService.getPrimaryByEmployee(1L)).thenReturn(
                EmpEmployeePosition.assign(1L, 11L, 100L, null, 1, null, null, Status.ENABLED));

        assertThrows(BizException.class,
                () -> fixture.resolver.resolveApprovers(1L, 10L, null));
    }

    private Fixture fixture() {
        EmpEmployeePositionService positionService = Mockito.mock(EmpEmployeePositionService.class);
        OrgNodeService nodeService = Mockito.mock(OrgNodeService.class);
        OrgGradeService gradeService = Mockito.mock(OrgGradeService.class);
        OrgNodeClosureRepository closureRepository = Mockito.mock(OrgNodeClosureRepository.class);
        when(positionService.getPrimaryByEmployee(1L)).thenReturn(
                EmpEmployeePosition.assign(1L, 10L, 100L, 1L, 1, null, null, Status.ENABLED));
        when(closureRepository.findAncestors(100L)).thenReturn(List.of(
                new OrgNodeClosure(100L, 100L, 0),
                new OrgNodeClosure(200L, 100L, 1)));
        return new Fixture(
                new SystemApprovalRouteResolver(
                        positionService, nodeService, gradeService, closureRepository),
                positionService, nodeService, gradeService);
    }

    private EmpEmployeePosition position(Long employeeId, Long gradeId) {
        return EmpEmployeePosition.assign(
                employeeId, 10L, null, gradeId, 1, null, null, Status.ENABLED);
    }

    private OrgNode node(Long id, Long managerId, String departmentFunction) {
        return OrgNode.create(
                10L, null, managerId, "Node", "DEPT", departmentFunction,
                "N" + id, 1, Status.ENABLED).setId(id);
    }

    private OrgGrade grade(Long id, String level) {
        return OrgGrade.create(10L, "Grade", "G" + id, level, 1, Status.ENABLED)
                .setId(id);
    }

    private record Fixture(
            SystemApprovalRouteResolver resolver,
            EmpEmployeePositionService positionService,
            OrgNodeService nodeService,
            OrgGradeService gradeService
    ) {
        private void nodes(Long directManager, Long parentManager) {
            when(nodeService.getById(100L)).thenReturn(node(100L, directManager, "SALES"));
            when(nodeService.getById(200L)).thenReturn(node(200L, parentManager, "GENERAL"));
        }

        private OrgNode node(Long id, Long managerId, String departmentFunction) {
            return OrgNode.create(
                    10L, null, managerId, "Node", "DEPT", departmentFunction,
                    "N" + id, 1, Status.ENABLED).setId(id);
        }
    }
}
