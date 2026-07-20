package com.manpowergroup.kintai.employee.application.assembler.emp;

import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionUpdateCommand;
import com.manpowergroup.kintai.employee.application.dto.emp.response.EmployeePositionResponse;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeePositionCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeePositionUpdateRequest;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;

public final class EmployeePositionAssembler {

    private EmployeePositionAssembler() {
    }

    public static EmployeePositionCreateCommand toCommand(EmployeePositionCreateRequest request) {
        return new EmployeePositionCreateCommand(
                request.getEmployeeId(), request.getCompanyId(), request.getNodeId(), request.getGradeId(),
                request.getIsPrimary(), request.getStartDate(), request.getEndDate(), request.getStatus()
        );
    }

    public static EmployeePositionUpdateCommand toCommand(EmployeePositionUpdateRequest request) {
        return new EmployeePositionUpdateCommand(
                request.getEmployeeId(), request.getCompanyId(), request.getNodeId(), request.getGradeId(),
                request.getIsPrimary(), request.getStartDate(), request.getEndDate(), request.getStatus()
        );
    }

    public static EmployeePositionResponse toResponse(EmpEmployeePosition position) {
        return EmployeePositionResponse.from(position);
    }
}
