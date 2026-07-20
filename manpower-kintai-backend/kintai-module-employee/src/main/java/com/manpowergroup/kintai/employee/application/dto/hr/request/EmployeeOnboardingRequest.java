package com.manpowergroup.kintai.employee.application.dto.hr.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "EmployeeOnboardingリクエスト")
public class EmployeeOnboardingRequest {

    @NotNull(message = "会社は必須です")
    @Schema(description = "会社ID")
    private Long companyId;

    @NotBlank(message = "社員番号は必須です")
    @Size(max = 50, message = "社員番号は50文字以内で入力してください")
    @Schema(description = "社員番号")
    private String employeeCode;

    @NotBlank(message = "姓は必須です")
    @Size(max = 50, message = "姓は50文字以内で入力してください")
    @Schema(description = "姓")
    private String lastName;

    @NotBlank(message = "名は必須です")
    @Size(max = 50, message = "名は50文字以内で入力してください")
    @Schema(description = "名")
    private String firstName;

    @Size(max = 50, message = "姓（カナ）は50文字以内で入力してください")
    @Schema(description = "姓（カナ）")
    private String lastNameKana;

    @Size(max = 50, message = "名（カナ）は50文字以内で入力してください")
    @Schema(description = "名（カナ）")
    private String firstNameKana;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 100, message = "メールアドレスは100文字以内で入力してください")
    @Schema(description = "メールアドレス")
    private String email;

    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    @Schema(description = "電話番号")
    private String phone;

    @Schema(description = "性別")
    private Integer gender;

    @NotNull(message = "入社日は必須です")
    @Schema(description = "入社日")
    private LocalDate hireDate;

    @NotNull(message = "所属組織は必須です")
    @Schema(description = "組織ノードID")
    private Long nodeId;

    @NotNull(message = "職級は必須です")
    @Schema(description = "職級ID")
    private Long gradeId;

    @NotEmpty(message = "ロールは1件以上選択してください")
    @Schema(description = "ロールIDリスト")
    private List<Long> roleIds;

    @NotBlank(message = "ユーザー名は必須です")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    @Schema(description = "ユーザー名")
    private String username;

    @NotBlank(message = "初期パスワードは必須です")
    @Size(min = 8, max = 100, message = "初期パスワードは8文字以上100文字以内で入力してください")
    @Schema(description = "パスワード")
    private String password;
}
