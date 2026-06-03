package com.manpowergroup.kintai.system.application.dto.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class I18nResponse {

    private Long id;
    private String refType;
    private Long refId;
    private String language;
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
