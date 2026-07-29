package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.EnumTypeCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumTypeUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.EnumTypeResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumTypeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumTypeUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;

public final class EnumTypeAssembler {

    private EnumTypeAssembler() {
    }

    /**
     * 入力データを列挙型作成コマンドへ変換する。
     *
     * @param request 変換対象の列挙型作成リクエスト
     * @return 変換後の列挙型作成コマンド
     */
    public static EnumTypeCreateCommand toCommand(EnumTypeCreateRequest request) {
        return new EnumTypeCreateCommand(
                request.getCode(),
                request.getName(),
                request.getRemark(),
                request.getSort(),
                request.getStatus()
        );
    }

    /**
     * 入力データを列挙型更新コマンドへ変換する。
     *
     * @param request 変換対象の列挙型更新リクエスト
     * @return 変換後の列挙型更新コマンド
     */
    public static EnumTypeUpdateCommand toCommand(EnumTypeUpdateRequest request) {
        return new EnumTypeUpdateCommand(
                request.getCode(),
                request.getName(),
                request.getRemark(),
                request.getSort(),
                request.getStatus()
        );
    }

    /**
     * 入力データを列挙型レスポンスへ変換する。
     *
     * @param enumType 変換対象の列挙型
     * @return 変換後の列挙型レスポンス
     */
    public static EnumTypeResponse toResponse(SysEnumType enumType) {
        return EnumTypeResponse.from(enumType);
    }
}
