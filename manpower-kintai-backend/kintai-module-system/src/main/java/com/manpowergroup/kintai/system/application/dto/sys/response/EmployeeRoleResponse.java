package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "EmployeeRoleレスポンス")
public class EmployeeRoleResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "社員ID")
    private Long employeeId;
    @Schema(description = "ロールID")
    private Long roleId;
    @Schema(description = "会社ID")
    private Long companyId;
    @Schema(description = "開始日")
    private LocalDate startDate;
    @Schema(description = "終了日")
    private LocalDate endDate;

    public static EmployeeRoleResponse from(SysEmployeeRole employeeRole) {
        return EmployeeRoleResponse.builder()
                .id(employeeRole.getId())
                .employeeId(employeeRole.getEmployeeId())
                .roleId(employeeRole.getRoleId())
                .companyId(employeeRole.getCompanyId())
                .startDate(employeeRole.getStartDate())
                .endDate(employeeRole.getEndDate())
                .build();
    }
}
