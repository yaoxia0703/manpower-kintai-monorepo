package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;

import java.util.List;

public interface SysPermissionRepository {

    List<SysPermission> loadEnabled();

    List<SysPermission> findAll();


}
