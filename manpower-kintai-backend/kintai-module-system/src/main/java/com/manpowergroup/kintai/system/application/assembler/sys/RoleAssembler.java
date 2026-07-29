package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.RoleResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

public final class RoleAssembler {

    private RoleAssembler() {
    }

    /**
     * 入力データをロール作成コマンドへ変換する。
     *
     * @param request 変換対象のロール作成リクエスト
     * @return 変換後のロール作成コマンド
     */
    public static RoleCreateCommand toCommand(RoleCreateRequest request) {
        return new RoleCreateCommand(
                request.getCompanyId(),
                request.getCode(),
                request.getName(),
                request.getRemark(),
                request.getSort()
        );
    }

    /**
     * 入力データをロール更新コマンドへ変換する。
     *
     * @param request 変換対象のロール更新リクエスト
     * @return 変換後のロール更新コマンド
     */
    public static RoleUpdateCommand toCommand(RoleUpdateRequest request) {
        return new RoleUpdateCommand(
                request.getCompanyId(),
                request.getCode(),
                request.getName(),
                request.getRemark(),
                request.getSort()
        );
    }

    /**
     * 入力データをロールレスポンスへ変換する。
     *
     * @param role 処理対象のロール
     * @return 変換後のロールレスポンス
     */
    public static RoleResponse toResponse(SysRole role) {
        return RoleResponse.from(role);
    }
}
