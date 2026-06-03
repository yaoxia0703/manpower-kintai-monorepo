package com.manpowergroup.kintai.system.application.dto.emp;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EmployeePositionResponse {

    private Long id;
    private Long employeeId;
    private Long companyId;
    private Long nodeId;
    private Long gradeId;
    private Integer isPrimary;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;

    public static EmployeePositionResponse from(EmpEmployeePosition position) {
        return EmployeePositionResponse.builder()
                .id(position.getId())
                .employeeId(position.getEmployeeId())
                .companyId(position.getCompanyId())
                .nodeId(position.getNodeId())
                .gradeId(position.getGradeId())
                .isPrimary(position.getIsPrimary())
                .startDate(position.getStartDate())
                .endDate(position.getEndDate())
                .status(position.getStatus())
                .build();
    }
}
