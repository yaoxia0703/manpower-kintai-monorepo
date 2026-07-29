package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.EnumValueCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.EnumValueResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumValueCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumValueUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;

public final class EnumValueAssembler {

    private EnumValueAssembler() {
    }

    /**
     * 入力データを列挙値作成コマンドへ変換する。
     *
     * @param request 変換対象の列挙値作成リクエスト
     * @return 変換後の列挙値作成コマンド
     */
    public static EnumValueCreateCommand toCommand(EnumValueCreateRequest request) {
        return new EnumValueCreateCommand(
                request.getEnumTypeCode(),
                request.getCode(),
                request.getSort(),
                request.getStatus()
        );
    }

    /**
     * 入力データを列挙値更新コマンドへ変換する。
     *
     * @param request 変換対象の列挙値更新リクエスト
     * @return 変換後の列挙値更新コマンド
     */
    public static EnumValueUpdateCommand toCommand(EnumValueUpdateRequest request) {
        return new EnumValueUpdateCommand(
                request.getEnumTypeCode(),
                request.getCode(),
                request.getSort(),
                request.getStatus()
        );
    }

    /**
     * 入力データを列挙値レスポンスへ変換する。
     *
     * @param enumValue 変換対象の列挙値
     * @return 変換後の列挙値レスポンス
     */
    public static EnumValueResponse toResponse(SysEnumValue enumValue) {
        return EnumValueResponse.from(enumValue);
    }
}
