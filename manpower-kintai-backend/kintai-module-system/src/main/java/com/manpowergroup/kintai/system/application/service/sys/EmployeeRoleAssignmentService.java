package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

public interface EmployeeRoleAssignmentService {

    SysEmployeeRole assign(EmployeeRoleAssignCommand command);

    SysEmployeeRole update(Long id, EmployeeRoleUpdateCommand command);

    void revoke(Long id);
}
