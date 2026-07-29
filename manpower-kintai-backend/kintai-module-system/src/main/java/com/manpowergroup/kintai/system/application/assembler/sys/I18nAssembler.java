package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.I18nUpsertCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.I18nResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.I18nUpsertRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;

public final class I18nAssembler {

    private I18nAssembler() {
    }

    /**
     * 入力データを多言語情報登録・更新コマンドへ変換する。
     *
     * @param request 変換対象の多言語情報Upsertリクエスト
     * @return 変換後の多言語情報登録・更新コマンド
     */
    public static I18nUpsertCommand toCommand(I18nUpsertRequest request) {
        return new I18nUpsertCommand(
                request.getRefType(),
                request.getRefId(),
                request.getLanguage(),
                request.getContent()
        );
    }

    /**
     * 入力データを多言語情報レスポンスへ変換する。
     *
     * @param i18n 変換対象の多言語情報
     * @return 変換後の多言語情報レスポンス
     */
    public static I18nResponse toResponse(SysI18n i18n) {
        return I18nResponse.from(i18n);
    }
}
