package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;

import java.util.List;

public interface SysRolePermissionRepository {
    List<SysRolePermission> findByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void save(SysRolePermission sysRolePermission);
}
