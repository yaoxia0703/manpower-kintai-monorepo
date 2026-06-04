package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

import java.util.List;

public interface SysEmployeeRoleService extends IService<SysEmployeeRole> {

    SysEmployeeRole getById(Long id);

    List<SysEmployeeRole> listActiveByEmployee(Long employeeId);

    SysEmployeeRole assign(EmployeeRoleAssignCommand command);

    SysEmployeeRole update(Long id, EmployeeRoleUpdateCommand command);

    void revoke(Long id);
}
