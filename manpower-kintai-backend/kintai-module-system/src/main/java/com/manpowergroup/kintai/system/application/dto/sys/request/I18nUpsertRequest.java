package com.manpowergroup.kintai.system.application.dto.sys.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class I18nUpsertRequest {

    @NotBlank(message = "参照種別は必須です")
    @Size(max = 50, message = "参照種別は50文字以内で入力してください")
    private String refType;

    @NotNull(message = "参照IDは必須です")
    private Long refId;

    @NotBlank(message = "言語コードは必須です")
    @Size(max = 20, message = "言語コードは20文字以内で入力してください")
    private String language;

    @NotBlank(message = "翻訳内容は必須です")
    @Size(max = 500, message = "翻訳内容は500文字以内で入力してください")
    private String content;
}
