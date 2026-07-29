package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;

import java.util.List;

public interface SysRolePermissionRepository {
    List<SysRolePermission> listByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void save(SysRolePermission sysRolePermission);

    // 複数ロールに紐づくロール権限関連を取得
    List<SysRolePermission> listByRoleIds(List<Long> roleIds);

    // 権限がいずれかのロールに割り当てられているか
    boolean existsByPermissionId(Long permissionId);
}
