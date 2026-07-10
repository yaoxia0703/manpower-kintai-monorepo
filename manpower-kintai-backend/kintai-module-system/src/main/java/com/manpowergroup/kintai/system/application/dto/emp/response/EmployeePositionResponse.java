package com.manpowergroup.kintai.system.application.dto.emp.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "EmployeePositionレスポンス")
public class EmployeePositionResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "社員ID")
    private Long employeeId;
    @Schema(description = "会社ID")
    private Long companyId;
    @Schema(description = "組織ノードID")
    private Long nodeId;
    @Schema(description = "職級ID")
    private Long gradeId;
    @Schema(description = "主務フラグ")
    private Integer isPrimary;
    @Schema(description = "開始日")
    private LocalDate startDate;
    @Schema(description = "終了日")
    private LocalDate endDate;
    @Schema(description = "ステータス")
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
