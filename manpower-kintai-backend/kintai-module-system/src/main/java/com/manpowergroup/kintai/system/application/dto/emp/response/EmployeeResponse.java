package com.manpowergroup.kintai.system.application.dto.emp.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Employeeレスポンス")
public class EmployeeResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "会社ID")
    private Long companyId;
    @Schema(description = "社員番号")
    private String employeeCode;
    @Schema(description = "姓")
    private String lastName;
    @Schema(description = "名")
    private String firstName;
    @Schema(description = "姓（カナ）")
    private String lastNameKana;
    @Schema(description = "名（カナ）")
    private String firstNameKana;
    @Schema(description = "メールアドレス")
    private String email;
    @Schema(description = "電話番号")
    private String phone;
    @Schema(description = "性別")
    private Integer gender;
    @Schema(description = "生年月日")
    private LocalDate birthDate;
    @Schema(description = "入社日")
    private LocalDate hireDate;
    @Schema(description = "退職日")
    private LocalDate leaveDate;
    @Schema(description = "ステータス")
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
