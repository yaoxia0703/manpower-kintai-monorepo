package com.manpowergroup.kintai.system.application.assembler.emp;

import com.manpowergroup.kintai.system.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.system.application.command.emp.EmployeeUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.emp.response.EmployeeResponse;
import com.manpowergroup.kintai.system.application.dto.emp.request.EmployeeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.emp.request.EmployeeUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;

public final class EmployeeAssembler {

    private EmployeeAssembler() {
    }

    public static EmployeeCreateCommand toCommand(EmployeeCreateRequest request) {
        return new EmployeeCreateCommand(
                request.getCompanyId(), request.getEmployeeCode(), request.getLastName(), request.getFirstName(),
                request.getLastNameKana(), request.getFirstNameKana(), request.getEmail(), request.getPhone(),
                request.getGender(), request.getBirthDate(), request.getHireDate(), request.getLeaveDate(),
                request.getStatus()
        );
    }

    public static EmployeeUpdateCommand toCommand(EmployeeUpdateRequest request) {
        return new EmployeeUpdateCommand(
                request.getCompanyId(), request.getEmployeeCode(), request.getLastName(), request.getFirstName(),
                request.getLastNameKana(), request.getFirstNameKana(), request.getEmail(), request.getPhone(),
                request.getGender(), request.getBirthDate(), request.getHireDate(), request.getLeaveDate(),
                request.getStatus()
        );
    }

    public static EmployeeResponse toResponse(EmpEmployee employee) {
        return EmployeeResponse.from(employee);
    }
}
