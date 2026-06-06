package com.manpowergroup.kintai.system.application.dto.sys.response;

import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EmployeeRoleResponse {

    private Long id;
    private Long employeeId;
    private Long roleId;
    private Long companyId;
    private LocalDate startDate;
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
