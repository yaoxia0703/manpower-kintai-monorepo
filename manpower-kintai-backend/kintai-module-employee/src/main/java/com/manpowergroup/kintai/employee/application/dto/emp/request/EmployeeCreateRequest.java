package com.manpowergroup.kintai.employee.application.dto.emp.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Employee作成リクエスト")
public class EmployeeCreateRequest {

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

    @Schema(description = "生年月日")
    private LocalDate birthDate;

    @NotNull(message = "入社日は必須です")
    @Schema(description = "入社日")
    private LocalDate hireDate;

    @Schema(description = "退職日")
    private LocalDate leaveDate;

    @Schema(description = "ステータス")
    private Status status;
}
