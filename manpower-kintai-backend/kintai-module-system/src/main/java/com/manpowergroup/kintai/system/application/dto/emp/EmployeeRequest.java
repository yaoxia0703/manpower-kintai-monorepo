package com.manpowergroup.kintai.system.application.dto.emp;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeRequest {

    @NotNull
    private Long companyId;

    @NotBlank
    @Size(max = 50)
    private String employeeCode;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastNameKana;

    @Size(max = 50)
    private String firstNameKana;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 20)
    private String phone;

    private Integer gender;

    private LocalDate birthDate;

    @NotNull
    private LocalDate hireDate;

    private LocalDate leaveDate;

    private Status status;

    public EmpEmployee toEntity() {
        return new EmpEmployee()
                .setCompanyId(companyId)
                .setEmployeeCode(employeeCode)
                .setLastName(lastName)
                .setFirstName(firstName)
                .setLastNameKana(lastNameKana)
                .setFirstNameKana(firstNameKana)
                .setEmail(email)
                .setPhone(phone)
                .setGender(gender)
                .setBirthDate(birthDate)
                .setHireDate(hireDate)
                .setLeaveDate(leaveDate)
                .setStatus(status == null ? Status.ENABLED : status);
    }
}
