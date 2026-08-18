package com.manpowergroup.kintai.hr.assembler.emp;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.employee.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.employee.application.command.hr.EmployeeOnboardingCommand;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingResponse;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterEntry;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterResult;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterValidationProvider;
import com.manpowergroup.kintai.hr.controller.emp.request.EmployeeRegisterRequest;
import com.manpowergroup.kintai.hr.controller.emp.response.EmployeeRegisterResponse;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;

import java.util.List;

public final class EmployeeRegisterAssembler {

    private EmployeeRegisterAssembler() {

    }


    public static EmployeeRegisterEntry toEntry(EmployeeRegisterRequest request) {
        return new EmployeeRegisterEntry(
            request.companyId(),
            request.employeeCode(),
            request.lastName(),
            request.firstName(),
            request.lastNameKana(),
            request.firstNameKana(),
            request.email(),
            request.phone(),
            request.gender(),
            request.hireDate(),
            request.nodeId(),
            request.gradeId(),
            request.roleIds(),
            request.username(),
            request.password()
        );
    }


    public static EmployeeRegisterResponse toResponse(EmployeeRegisterResult response) {
        return new EmployeeRegisterResponse(
            response.employeeId(),
            response.accountId(),
            response.positionId(),
            response.employeeCode(),
            response.displayName(),
            response.email()
        );
    }

    public static EmployeeRegisterValidationProvider.OperatorInfo toOperatorInfo(EmpEmployee employee) {
        return new EmployeeRegisterValidationProvider.OperatorInfo(
            employee.getId(),
            employee.getCompanyId(),
            employee.getStatus()
        );
    }

    public static EmployeeCreateCommand toEmployeeCreateCommand(EmployeeRegisterEntry entry) {
        return new EmployeeCreateCommand(
            entry.companyId(),
            entry.employeeCode(),
            entry.lastName(),
            entry.firstName(),
            entry.lastNameKana(),
            entry.firstNameKana(),
            entry.email(),
            entry.phone(),
            entry.gender(),
            null,
            entry.hireDate(),
            null,
            Status.ENABLED
        );
    }

    public static AccountCreateCommand toAccountCreateCommand(Long employeeId, EmployeeRegisterEntry entry) {
        return new AccountCreateCommand(
            employeeId,
            entry.username(),
            entry.password()
        );
    }

    public static EmployeePositionCreateCommand toEmployeePositionCreateCommand(Long employeeId, EmployeeRegisterEntry entry) {
        return new EmployeePositionCreateCommand(
            employeeId,
            entry.companyId(),
            entry.nodeId(),
            entry.gradeId(),
            1, // isPrimary
            entry.hireDate(),
            null, // endDate
            Status.ENABLED
        );
    }

    public static List<EmployeeRoleAssignCommand> toEmployeeRoles(Long employeeId, EmployeeRegisterEntry entry) {
        return entry.roleIds().stream()
            .map(roleId -> new EmployeeRoleAssignCommand(
                employeeId,
                roleId,
                entry.companyId(),
                entry.hireDate(),
                null
            ))
            .toList();
    }
}
