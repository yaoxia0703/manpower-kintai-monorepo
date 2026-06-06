package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.EmployeeRoleResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EmployeeRoleAssignRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EmployeeRoleUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

public final class EmployeeRoleAssembler {

    private EmployeeRoleAssembler() {
    }

    public static EmployeeRoleAssignCommand toCommand(EmployeeRoleAssignRequest request) {
        return new EmployeeRoleAssignCommand(
                request.getEmployeeId(),
                request.getRoleId(),
                request.getCompanyId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    public static EmployeeRoleUpdateCommand toCommand(EmployeeRoleUpdateRequest request) {
        return new EmployeeRoleUpdateCommand(
                request.getEmployeeId(),
                request.getRoleId(),
                request.getCompanyId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    public static EmployeeRoleResponse toResponse(SysEmployeeRole employeeRole) {
        return EmployeeRoleResponse.from(employeeRole);
    }
}
