package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;

import java.util.List;

public interface SysMenuRepository {

    List<SysMenu> findAll();
}
