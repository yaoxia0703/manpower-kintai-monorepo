package com.manpowergroup.kintai.system.application.dto.sys.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "EnumValue作成リクエスト")
public class EnumValueCreateRequest {

    @NotBlank(message = "列挙型コードは必須です")
    @Size(max = 50, message = "列挙型コードは50文字以内で入力してください")
    @Schema(description = "列挙型コード")
    private String enumTypeCode;

    @NotBlank(message = "列挙値コードは必須です")
    @Size(max = 50, message = "列挙値コードは50文字以内で入力してください")
    @Schema(description = "コード")
    private String code;

    @NotNull(message = "表示順は必須です")
    @Schema(description = "表示順")
    private Integer sort;

    @Schema(description = "ステータス")
    private Status status;
}
