package com.manpowergroup.kintai.system.application.service.impl.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.application.dto.manager.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.system.application.service.manager.ManagerSubordinateService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeePositionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgGradeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeClosureMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerSubordinateServiceImpl implements ManagerSubordinateService {

    private final OrgNodeMapper nodeMapper;
    private final OrgNodeClosureMapper closureMapper;
    private final EmpEmployeePositionMapper positionMapper;
    private final EmpEmployeeMapper employeeMapper;
    private final OrgGradeMapper gradeMapper;

    @Override
    public List<SubordinateEmployeeResponse> listSubordinates(Long managerEmployeeId) {
        List<Long> managedNodeIds = nodeMapper.selectList(Wrappers.<OrgNode>lambdaQuery()
                        .eq(OrgNode::getManagerId, managerEmployeeId)
                        .eq(OrgNode::getStatus, Status.ENABLED))
                .stream()
                .map(OrgNode::getId)
                .toList();
        if (managedNodeIds.isEmpty()) return Collections.emptyList();

        List<Long> descendantNodeIds = closureMapper.selectList(Wrappers.<OrgNodeClosure>lambdaQuery()
                        .in(OrgNodeClosure::getAncestorId, managedNodeIds))
                .stream()
                .map(OrgNodeClosure::getDescendantId)
                .distinct()
                .toList();
        if (descendantNodeIds.isEmpty()) return Collections.emptyList();

        LocalDate today = LocalDate.now();
        List<EmpEmployeePosition> positions = positionMapper.selectList(Wrappers.<EmpEmployeePosition>lambdaQuery()
                .in(EmpEmployeePosition::getNodeId, descendantNodeIds)
                .eq(EmpEmployeePosition::getStatus, Status.ENABLED)
                .le(EmpEmployeePosition::getStartDate, today)
                .and(q -> q.isNull(EmpEmployeePosition::getEndDate)
                        .or()
                        .ge(EmpEmployeePosition::getEndDate, today)));
        if (positions.isEmpty()) return Collections.emptyList();

        Map<Long, EmpEmployeePosition> primaryPositionByEmployee = positions.stream()
                .filter(position -> !Objects.equals(position.getEmployeeId(), managerEmployeeId))
                .collect(Collectors.toMap(
                        EmpEmployeePosition::getEmployeeId,
                        position -> position,
                        (left, right) -> Objects.equals(right.getIsPrimary(), 1) ? right : left
                ));
        if (primaryPositionByEmployee.isEmpty()) return Collections.emptyList();

        Map<Long, String> nodeNameById = nodeMapper.selectBatchIds(descendantNodeIds)
                .stream()
                .collect(Collectors.toMap(OrgNode::getId, OrgNode::getName, (left, right) -> left));
        List<Long> gradeIds = primaryPositionByEmployee.values()
                .stream()
                .map(EmpEmployeePosition::getGradeId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, String> gradeNameById = gradeIds.isEmpty()
                ? Collections.emptyMap()
                : gradeMapper.selectBatchIds(gradeIds)
                .stream()
                .collect(Collectors.toMap(OrgGrade::getId, OrgGrade::getName, (left, right) -> left));

        return employeeMapper.selectList(Wrappers.<EmpEmployee>lambdaQuery()
                        .in(EmpEmployee::getId, primaryPositionByEmployee.keySet())
                        .eq(EmpEmployee::getStatus, Status.ENABLED))
                .stream()
                .map(employee -> toResponse(
                        employee,
                        primaryPositionByEmployee.get(employee.getId()),
                        nodeNameById,
                        gradeNameById))
                .toList();
    }

    private SubordinateEmployeeResponse toResponse(
            EmpEmployee employee,
            EmpEmployeePosition position,
            Map<Long, String> nodeNameById,
            Map<Long, String> gradeNameById) {
        return SubordinateEmployeeResponse.builder()
                .employeeId(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .displayName(employee.getLastName() + " " + employee.getFirstName())
                .email(employee.getEmail())
                .companyId(employee.getCompanyId())
                .nodeId(position.getNodeId())
                .nodeName(nodeNameById.get(position.getNodeId()))
                .gradeId(position.getGradeId())
                .gradeName(gradeNameById.get(position.getGradeId()))
                .build();
    }
}
