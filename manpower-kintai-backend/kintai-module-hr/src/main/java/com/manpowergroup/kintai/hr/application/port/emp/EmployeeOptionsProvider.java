package com.manpowergroup.kintai.hr.application.port.emp;

public interface EmployeeOptionsProvider {

    EmployeeOptionsResult getEmployeeOptions(Long operatorEmployeeId, Long companyId);
}
