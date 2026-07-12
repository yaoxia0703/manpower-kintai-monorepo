package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.EmployeeRoleAssignmentService;
import com.manpowergroup.kintai.system.application.service.sys.SysEmployeeRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeRoleAssignmentServiceImpl implements EmployeeRoleAssignmentService {

    private final SysEmployeeRoleService employeeRoleService;

    @Override
    @Transactional
    public SysEmployeeRole assign(EmployeeRoleAssignCommand command) {
        boolean exists = employeeRoleService.lambdaQuery()
                .eq(SysEmployeeRole::getEmployeeId, command.employeeId())
                .eq(SysEmployeeRole::getRoleId, command.roleId())
                .eq(SysEmployeeRole::getCompanyId, command.companyId())
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.EMPLOYEE_ROLE_ALREADY_EXISTS);
        SysEmployeeRole employeeRole = SysEmployeeRole.assign(
                command.employeeId(),
                command.roleId(),
                command.companyId(),
                command.startDate(),
                command.endDate());
        employeeRoleService.save(employeeRole);
        return employeeRole;
    }

    @Override
    @Transactional
    public SysEmployeeRole update(Long id, EmployeeRoleUpdateCommand command) {
        SysEmployeeRole existing = employeeRoleService.getById(id);
        existing.changeValidity(command.startDate(), command.endDate());
        employeeRoleService.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void revoke(Long id) {
        employeeRoleService.getById(id);
        employeeRoleService.removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        EMPLOYEE_ROLE_ALREADY_EXISTS(409, "error.employee_role.already_exists");

        private final int code;
        private final String messageKey;

        SystemErrorCode(int code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}
