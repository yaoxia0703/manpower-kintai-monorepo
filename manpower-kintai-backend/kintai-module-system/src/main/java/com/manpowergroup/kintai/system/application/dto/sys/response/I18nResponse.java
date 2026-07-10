package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "I18nレスポンス")
public class I18nResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "関連業務タイプ")
    private String refType;
    @Schema(description = "関連業務ID")
    private Long refId;
    @Schema(description = "言語コード")
    private String language;
    @Schema(description = "内容")
    private String content;

    public static I18nResponse from(SysI18n i18n) {
        return I18nResponse.builder()
                .id(i18n.getId())
                .refType(i18n.getRefType())
                .refId(i18n.getRefId())
                .language(i18n.getLanguage())
                .content(i18n.getContent())
                .build();
    }
}
