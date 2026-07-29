package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.PermissionCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.PermissionUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.PermissionResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.PermissionCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.PermissionUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;

public final class PermissionAssembler {

    private PermissionAssembler() {
    }

    /**
     * 入力データを権限作成コマンドへ変換する。
     *
     * @param request 変換対象の権限作成リクエスト
     * @return 変換後の権限作成コマンド
     */
    public static PermissionCreateCommand toCommand(PermissionCreateRequest request) {
        return new PermissionCreateCommand(
                request.getMenuId(),
                request.getCode(),
                request.getName(),
                request.getMethod(),
                request.getPath(),
                request.getRemark(),
                request.getSort()
        );
    }

    /**
     * 入力データを権限更新コマンドへ変換する。
     *
     * @param request 変換対象の権限更新リクエスト
     * @return 変換後の権限更新コマンド
     */
    public static PermissionUpdateCommand toCommand(PermissionUpdateRequest request) {
        return new PermissionUpdateCommand(
                request.getMenuId(),
                request.getCode(),
                request.getName(),
                request.getMethod(),
                request.getPath(),
                request.getRemark(),
                request.getSort()
        );
    }

    /**
     * 入力データを権限レスポンスへ変換する。
     *
     * @param permission 処理対象の権限
     * @return 変換後の権限レスポンス
     */
    public static PermissionResponse toResponse(SysPermission permission) {
        return PermissionResponse.from(permission);
    }
}
