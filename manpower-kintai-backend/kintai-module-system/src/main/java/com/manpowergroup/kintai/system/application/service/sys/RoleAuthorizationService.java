package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleAuthorizationSaveCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleMenuAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.RolePermissionAssignCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.RoleAuthorizationResponse;

public interface RoleAuthorizationService {

    /**
     * 指定ロールの認可設定を取得する。
     *
     * @param roleId 対象のロールID
     * @return 取得したロール認可設定レスポンス
     */
    RoleAuthorizationResponse getAuthorization(Long roleId);

    /**
     * ロール認可設定を割り当てる。
     *
     * @param command 処理対象のロールメニュー割当割当コマンド
     */
    void assignMenus(RoleMenuAssignCommand command);

    /**
     * ロール認可設定を割り当てる。
     *
     * @param command 処理対象のロール権限割当割当コマンド
     */
    void assignPermissions(RolePermissionAssignCommand command);

    /**
     * ロール認可設定を保存する。
     *
     * @param command 処理対象のロール認可設定保存コマンド
     */
    void saveAuthorization(RoleAuthorizationSaveCommand command);
}
