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

    @NotNull(message = "入社日は必須です")
    private LocalDate hireDate;

    @NotNull(message = "所属組織は必須です")
    private Long nodeId;

    @NotNull(message = "職級は必須です")
    private Long gradeId;

    @NotEmpty(message = "ロールは1件以上選択してください")
    private List<Long> roleIds;

    @NotBlank(message = "ユーザー名は必須です")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    private String username;

    @NotBlank(message = "初期パスワードは必須です")
    @Size(min = 8, max = 100, message = "初期パスワードは8文字以上100文字以内で入力してください")
    private String password;
}
