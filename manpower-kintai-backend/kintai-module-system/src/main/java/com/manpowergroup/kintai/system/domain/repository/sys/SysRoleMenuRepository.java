package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuRepository {

    /**
     * ロールIDに一致するロールメニュー割当を一覧取得する。
     *
     * @param roleId 対象のロールID
     * @return ロールメニュー割当一覧。該当しない場合は空リスト
     */
    List<SysRoleMenu>  listByRoleId(Long roleId);

    /**
     * ロールIDに対応するロールメニュー割当を削除する。
     *
     * @param roleId 対象のロールID
     */
    void deleteByRoleId(Long roleId);

    /**
     * ロールメニュー割当を保存する。
     *
     * @param sysRoleMenu 保存対象のロールメニュー割当
     */
    void save(SysRoleMenu sysRoleMenu);

    /**
     * ロールID一覧に一致するロールメニュー割当を一覧取得する。
     *
     * @param roleIds 対象のロールID一覧
     * @return ロールメニュー割当一覧。該当しない場合は空リスト
     */
    List<SysRoleMenu> listByRoleIds(List<Long> roleIds);

    /**
     * メニューID一覧に対応するロールメニュー割当を削除する。
     *
     * @param menuIds 対象のメニューID一覧
     */
    void deleteByMenuIds(List<Long> menuIds);
}
