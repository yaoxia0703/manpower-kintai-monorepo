package com.manpowergroup.kintai.employee.application.dto.org.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Grade作成リクエスト")
public class GradeCreateRequest {

    @NotNull(message = "会社は必須です")
    @Schema(description = "会社ID")
    private Long companyId;

    @NotBlank(message = "職級名は必須です")
    @Size(max = 100, message = "職級名は100文字以内で入力してください")
    @Schema(description = "名称")
    private String name;

    @NotBlank(message = "職級コードは必須です")
    @Size(max = 50, message = "職級コードは50文字以内で入力してください")
    @Schema(description = "コード")
    private String code;

    @NotBlank(message = "職級レベルは必須です")
    @Size(max = 50, message = "職級レベルは50文字以内で入力してください")
    @Schema(description = "職級レベル")
    private String gradeLevel;

    @NotNull(message = "表示順は必須です")
    @Schema(description = "表示順")
    private Integer sort;

    @Schema(description = "ステータス")
    private Status status;
}
