package com.manpowergroup.kintai.system.application.dto.emp;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EmployeeResponse {

    private Long id;
    private Long companyId;
    private String employeeCode;
    private String lastName;
    private String firstName;
    private String lastNameKana;
    private String firstNameKana;
    private String email;
    private String phone;
    private Integer gender;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private LocalDate leaveDate;
    private Status status;

    public static EmployeeResponse from(EmpEmployee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .companyId(employee.getCompanyId())
                .employeeCode(employee.getEmployeeCode())
                .lastName(employee.getLastName())
                .firstName(employee.getFirstName())
                .lastNameKana(employee.getLastNameKana())
                .firstNameKana(employee.getFirstNameKana())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .gender(employee.getGender())
                .birthDate(employee.getBirthDate())
                .hireDate(employee.getHireDate())
                .leaveDate(employee.getLeaveDate())
                .status(employee.getStatus())
                .build();
    }
}
