package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.I18nUpsertCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.I18nResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.I18nUpsertRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;

public final class I18nAssembler {

    private I18nAssembler() {
    }

    public static I18nUpsertCommand toCommand(I18nUpsertRequest request) {
        return new I18nUpsertCommand(
                request.getRefType(),
                request.getRefId(),
                request.getLanguage(),
                request.getContent()
        );
    }

    public static I18nResponse toResponse(SysI18n i18n) {
        return I18nResponse.from(i18n);
    }
}
