package com.manpowergroup.kintai.system.application.dto.emp.request;

import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeCreateRequest {

    @NotNull(message = "会社は必須です")
    private Long companyId;

    @NotBlank(message = "社員番号は必須です")
    @Size(max = 50, message = "社員番号は50文字以内で入力してください")
    private String employeeCode;

    @NotBlank(message = "姓は必須です")
    @Size(max = 50, message = "姓は50文字以内で入力してください")
    private String lastName;

    @NotBlank(message = "名は必須です")
    @Size(max = 50, message = "名は50文字以内で入力してください")
    private String firstName;

    @Size(max = 50, message = "姓（カナ）は50文字以内で入力してください")
    private String lastNameKana;

    @Size(max = 50, message = "名（カナ）は50文字以内で入力してください")
    private String firstNameKana;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 100, message = "メールアドレスは100文字以内で入力してください")
    private String email;

    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    private String phone;

    private Integer gender;

    private LocalDate birthDate;

    @NotNull(message = "入社日は必須です")
    private LocalDate hireDate;

    private LocalDate leaveDate;

    private Status status;
}
