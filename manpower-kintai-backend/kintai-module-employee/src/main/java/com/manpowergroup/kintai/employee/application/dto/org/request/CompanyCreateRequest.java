package com.manpowergroup.kintai.employee.application.dto.org.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Company作成リクエスト")
public class CompanyCreateRequest {

    @Schema(description = "親ID")
    private Long parentId;

    @NotBlank(message = "会社名は必須です")
    @Size(max = 100, message = "会社名は100文字以内で入力してください")
    @Schema(description = "名称")
    private String name;

    @NotBlank(message = "会社コードは必須です")
    @Size(max = 50, message = "会社コードは50文字以内で入力してください")
    @Schema(description = "会社コード")
    private String companyCode;

    @NotNull(message = "階層レベルは必須です")
    @Schema(description = "階層レベル")
    private Integer level;

    @NotNull(message = "表示順は必須です")
    @Schema(description = "表示順")
    private Integer sort;

    @Schema(description = "ステータス")
    private Status status;
}
