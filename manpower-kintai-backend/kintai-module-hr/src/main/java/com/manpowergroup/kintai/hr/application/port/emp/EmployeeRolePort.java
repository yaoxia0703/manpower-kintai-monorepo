package com.manpowergroup.kintai.hr.application.port.emp;

public interface EmployeeRolePort {

    void assignRoles(Long employeeId, Long roleId,EmployeeRegisterEntry entry);
}
