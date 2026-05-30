package com.manpowergroup.kintai.system.application.dto.hr;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeOnboardingRequest {

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

    @NotNull
    private LocalDate hireDate;

    @NotNull
    private Long nodeId;

    @NotNull
    private Long gradeId;

    @NotEmpty
    private List<Long> roleIds;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}
