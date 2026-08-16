package com.manpowergroup.kintai.hr.controller.emp.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "新社員登録リクエスト")
public record EmployeeRegisterRequest(
    @NotNull(message = "会社は必須です")
    @Schema(description = "会社ID")
    Long companyId,

    @NotBlank(message = "社員番号は必須です")
    @Size(max = 50, message = "社員番号は50文字以内で入力してください")
    @Schema(description = "社員番号")
    String employeeCode,

    @NotBlank(message = "姓は必須です")
    @Size(max = 50, message = "姓は50文字以内で入力してください")
    @Schema(description = "姓")
    String lastName,

    @NotBlank(message = "名は必須です")
    @Size(max = 50, message = "名は50文字以内で入力してください")
    @Schema(description = "名")
    String firstName,

    @Size(max = 50, message = "姓（カナ）は50文字以内で入力してください")
    @Schema(description = "姓（カナ）")
    String lastNameKana,

    @Size(max = 50, message = "名（カナ）は50文字以内で入力してください")
    @Schema(description = "名（カナ）")
    String firstNameKana,

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    @Size(max = 100, message = "メールアドレスは100文字以内で入力してください")
    @Schema(description = "メールアドレス")
    String email,

    @Size(max = 20, message = "電話番号は20文字以内で入力してください")
    @Schema(description = "電話番号")
    String phone,

    @Schema(description = "性別")
    Integer gender,

    @NotNull(message = "入社日は必須です")
    @Schema(description = "入社日")
    LocalDate hireDate,

    @NotNull(message = "所属組織は必須です")
    @Schema(description = "組織ノードID")
    Long nodeId,

    @NotNull(message = "職級は必須です")
    @Schema(description = "職級ID")
    Long gradeId,

    @NotEmpty(message = "ロールは1件以上選択してください")
    @Schema(description = "ロールIDリスト")
    List<Long> roleIds,

    @NotBlank(message = "ユーザー名は必須です")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    @Schema(description = "ユーザー名")
    String username,

    @NotBlank(message = "初期パスワードは必須です")
    @Size(min = 8, max = 100, message = "初期パスワードは8文字以上100文字以内で入力してください")
    @Schema(description = "パスワード")
    String password
) {
}
