package com.manpowergroup.kintai.hr.application.port.emp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "新社員登録エントリー")
public record EmployeeRegisterEntry(

    Long companyId,

    String employeeCode,

    String lastName,

    String firstName,

    String lastNameKana,

    String firstNameKana,

    String email,

    String phone,

    Integer gender,

    LocalDate hireDate,

    Long nodeId,

    Long gradeId,

    List<Long> roleIds,

    String username,

    String password

) {

}
