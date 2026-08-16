package com.manpowergroup.kintai.hr.application.port.emp;

public interface EmployeeRegistrationPort {
    EmployeeRegisterResult createEmployee(EmployeeRegisterEntry entry);

    Long createAccount();

    Long createPosition();
}
