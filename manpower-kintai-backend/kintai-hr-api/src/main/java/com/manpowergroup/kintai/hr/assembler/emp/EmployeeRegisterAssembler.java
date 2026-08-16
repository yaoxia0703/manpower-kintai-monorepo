package com.manpowergroup.kintai.hr.assembler.emp;

import com.manpowergroup.kintai.employee.application.command.hr.EmployeeOnboardingCommand;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingResponse;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterEntry;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterResult;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterValidationPort;
import com.manpowergroup.kintai.hr.controller.emp.request.EmployeeRegisterRequest;
import com.manpowergroup.kintai.hr.controller.emp.response.EmployeeRegisterResponse;

public final class EmployeeRegisterAssembler {

    private EmployeeRegisterAssembler() {

    }


    public static EmployeeOnboardingCommand toEmployeeOnboardingCommand(EmployeeRegisterEntry entry) {
        return new EmployeeOnboardingCommand(
            entry.companyId(),
            entry.employeeCode(),
            entry.lastName(),
            entry.firstName(),
            entry.lastNameKana(),
            entry.firstNameKana(),
            entry.email(),
            entry.phone(),
            entry.gender(),
            entry.hireDate(),
            entry.nodeId(),
            entry.gradeId(),
            entry.roleIds(),
            entry.username(),
            entry.password()
        );
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

    public static EmployeeRegisterResult toResult(EmployeeOnboardingResponse response) {
        return new EmployeeRegisterResult(
            response.getEmployeeId(),
            response.getAccountId(),
            response.getPositionId(),
            response.getEmployeeCode(),
            response.getDisplayName(),
            response.getEmail()
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

    public static EmployeeRegisterValidationPort.OperatorInfo toOperatorInfo(EmpEmployee employee) {
        return new EmployeeRegisterValidationPort.OperatorInfo(
            employee.getId(),
            employee.getCompanyId(),
            employee.getStatus()
        );
    }
}
