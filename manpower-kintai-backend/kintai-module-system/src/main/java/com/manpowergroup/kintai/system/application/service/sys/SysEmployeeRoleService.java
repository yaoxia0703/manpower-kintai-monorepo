package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

import java.util.List;

public interface SysEmployeeRoleService  {

    SysEmployeeRole getById(Long id);

    List<SysEmployeeRole> listActiveByEmployee(Long employeeId);
}
