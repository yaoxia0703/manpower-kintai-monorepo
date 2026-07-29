package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuRepository {

    List<SysRoleMenu>  listByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void save(SysRoleMenu sysRoleMenu);

    List<SysRoleMenu> listByRoleIds(List<Long> roleIds);

    void deleteByMenuIds(List<Long> menuIds);
}
