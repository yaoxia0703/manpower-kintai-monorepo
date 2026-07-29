package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleAuthorizationSaveCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleMenuAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.RolePermissionAssignCommand;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleAssignRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleAuthorizationSaveRequest;

public final class RoleAuthorizationAssembler {

    private RoleAuthorizationAssembler() {
    }

    /**
     * 入力データをロールメニュー割当割当コマンドへ変換する。
     *
     * @param roleId 対象のロールID
     * @param request 変換対象のロール割当リクエスト
     * @return 変換後のロールメニュー割当割当コマンド
     */
    public static RoleMenuAssignCommand toMenuAssignCommand(Long roleId, RoleAssignRequest request) {
        return new RoleMenuAssignCommand(roleId, request == null ? null : request.getIds());
    }

    /**
     * 入力データをロール権限割当割当コマンドへ変換する。
     *
     * @param roleId 対象のロールID
     * @param request 変換対象のロール割当リクエスト
     * @return 変換後のロール権限割当割当コマンド
     */
    public static RolePermissionAssignCommand toPermissionAssignCommand(Long roleId, RoleAssignRequest request) {
        return new RolePermissionAssignCommand(roleId, request == null ? null : request.getIds());
    }

    /**
     * 入力データをロール認可設定保存コマンドへ変換する。
     *
     * @param roleId 対象のロールID
     * @param request 変換対象のロール認可設定保存リクエスト
     * @return 変換後のロール認可設定保存コマンド
     */
    public static RoleAuthorizationSaveCommand toSaveCommand(Long roleId, RoleAuthorizationSaveRequest request) {
        return new RoleAuthorizationSaveCommand(
                roleId,
                request == null ? null : request.getMenuIds(),
                request == null ? null : request.getPermissionIds()
        );
    }
}
