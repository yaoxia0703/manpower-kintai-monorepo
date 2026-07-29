package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;

import java.util.List;

public interface SysRolePermissionRepository {
    /**
     * ロールIDに一致するロール権限割当を一覧取得する。
     *
     * @param roleId 対象のロールID
     * @return ロール権限割当一覧。該当しない場合は空リスト
     */
    List<SysRolePermission> listByRoleId(Long roleId);

    /**
     * ロールIDに対応するロール権限割当を削除する。
     *
     * @param roleId 対象のロールID
     */
    void deleteByRoleId(Long roleId);

    /**
     * ロール権限割当を保存する。
     *
     * @param sysRolePermission 保存対象のロール権限割当
     */
    void save(SysRolePermission sysRolePermission);

    /**
     * ロールID一覧に一致するロール権限割当を一覧取得する。
     *
     * @param roleIds 対象のロールID一覧
     * @return ロール権限割当一覧。該当しない場合は空リスト
     */
    List<SysRolePermission> listByRoleIds(List<Long> roleIds);

    /**
     * 権限IDに一致するロール権限割当が存在するか判定する。
     *
     * @param permissionId 対象の権限ID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByPermissionId(Long permissionId);
}
