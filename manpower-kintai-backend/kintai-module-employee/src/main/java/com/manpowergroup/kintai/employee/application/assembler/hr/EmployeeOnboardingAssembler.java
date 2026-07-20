package com.manpowergroup.kintai.employee.application.assembler.hr;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.employee.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;
import com.manpowergroup.kintai.employee.application.dto.hr.request.EmployeeOnboardingRequest;

import java.util.List;

public final class EmployeeOnboardingAssembler {

    private EmployeeOnboardingAssembler() {
    }

    public static EmployeeCreateCommand toEmployee(EmployeeOnboardingRequest request, Long operatorEmployeeId) {
        return new EmployeeCreateCommand(
                request.getCompanyId(),
                request.getEmployeeCode(),
                request.getLastName(),
                request.getFirstName(),
                request.getLastNameKana(),
                request.getFirstNameKana(),
                request.getEmail(),
                request.getPhone(),
                request.getGender(),
                null,
                request.getHireDate(),
                null,
                Status.ENABLED
        );
    }

    public static AccountCreateCommand toAccount(EmployeeOnboardingRequest request, Long employeeId, Long operatorEmployeeId) {
        return new AccountCreateCommand(
                employeeId,
                request.getUsername(),
                request.getPassword()
        );
    }

    public static EmployeePositionCreateCommand toPosition(EmployeeOnboardingRequest request, Long employeeId, Long operatorEmployeeId) {
        return new EmployeePositionCreateCommand(
                employeeId,
                request.getCompanyId(),
                request.getNodeId(),
                request.getGradeId(),
                1,
                request.getHireDate(),
                null,
                Status.ENABLED
        );
    }

    public static List<EmployeeRoleAssignCommand> toEmployeeRoles(EmployeeOnboardingRequest request, Long employeeId, Long operatorEmployeeId) {
        return request.getRoleIds().stream()
                .map(roleId -> new EmployeeRoleAssignCommand(
                        employeeId,
                        roleId,
                        request.getCompanyId(),
                        request.getHireDate(),
                        null
                ))
                .toList();
    }
}
