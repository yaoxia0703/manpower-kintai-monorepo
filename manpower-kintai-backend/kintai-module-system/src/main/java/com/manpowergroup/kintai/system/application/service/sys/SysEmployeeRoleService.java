package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

import java.util.List;

public interface SysEmployeeRoleService extends IService<SysEmployeeRole> {

    SysEmployeeRole getById(Long id);

    List<SysEmployeeRole> listActiveByEmployee(Long employeeId);
}
