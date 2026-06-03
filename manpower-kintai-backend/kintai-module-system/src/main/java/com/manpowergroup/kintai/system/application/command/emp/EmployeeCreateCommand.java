package com.manpowergroup.kintai.system.application.command.emp;

import com.manpowergroup.kintai.common.enums.Status;

import java.time.LocalDate;

public record EmployeeCreateCommand(
        Long companyId,
        String employeeCode,
        String lastName,
        String firstName,
        String lastNameKana,
        String firstNameKana,
        String email,
        String phone,
        Integer gender,
        LocalDate birthDate,
        LocalDate hireDate,
        LocalDate leaveDate,
        Status status
) {
}
