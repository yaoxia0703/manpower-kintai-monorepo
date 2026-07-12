package com.manpowergroup.kintai.system.domain.model.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;

import java.util.List;
import java.util.Objects;

/** ロールに割り当てるメニューと権限の正規化済み集合を表す。 */
public record RoleAuthorization(
        Long roleId,
        List<Long> menuIds,
        List<Long> permissionIds
) {

    public RoleAuthorization {
        menuIds = normalize(menuIds);
        permissionIds = normalize(permissionIds);
    }

    /** 重複と null を除去したロール権限を生成する。 */
    public static RoleAuthorization replace(Long roleId, List<Long> menuIds, List<Long> permissionIds) {
        return new RoleAuthorization(roleId, menuIds, permissionIds);
    }

    /** メニュー割当を永続化用の関連エンティティへ変換する。 */
    public List<SysRoleMenu> toRoleMenus() {
        return menuIds.stream()
                .map(menuId -> SysRoleMenu.link(roleId, menuId))
                .toList();
    }

    /** 権限割当を永続化用の関連エンティティへ変換する。 */
    public List<SysRolePermission> toRolePermissions() {
        return permissionIds.stream()
                .map(permissionId -> SysRolePermission.link(roleId, permissionId))
                .toList();
    }

    private static List<Long> normalize(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }
}
