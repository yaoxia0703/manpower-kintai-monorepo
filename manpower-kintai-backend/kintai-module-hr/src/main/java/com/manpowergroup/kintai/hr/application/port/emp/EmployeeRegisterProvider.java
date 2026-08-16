package com.manpowergroup.kintai.hr.application.port.emp;

public interface EmployeeRegisterProvider {

    EmployeeRegisterResult registerEmployee(EmployeeRegisterEntry entry,Long employeeId);
}
