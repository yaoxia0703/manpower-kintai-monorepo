package com.manpowergroup.kintai.system.application.dto.sys.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "I18nUpsertリクエスト")
public class I18nUpsertRequest {

    @NotBlank(message = "参照種別は必須です")
    @Size(max = 50, message = "参照種別は50文字以内で入力してください")
    @Schema(description = "関連業務タイプ")
    private String refType;

    @NotNull(message = "参照IDは必須です")
    @Schema(description = "関連業務ID")
    private Long refId;

    @NotBlank(message = "言語コードは必須です")
    @Size(max = 20, message = "言語コードは20文字以内で入力してください")
    @Schema(description = "言語コード")
    private String language;

    @NotBlank(message = "翻訳内容は必須です")
    @Size(max = 500, message = "翻訳内容は500文字以内で入力してください")
    @Schema(description = "内容")
    private String content;
}
