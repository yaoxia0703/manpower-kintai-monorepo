package com.manpowergroup.kintai.employee.application.service.emp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.employee.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.AccountUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;

public interface EmpAccountService extends IService<EmpAccount> {

    EmpAccount getById(Long id);

    EmpAccount getByEmployeeId(Long employeeId);

    EmpAccount create(AccountCreateCommand command);

    EmpAccount update(Long id, AccountUpdateCommand command);

    void changePassword(Long id, String oldPassword, String newPassword);

    void enable(Long id);

    void disable(Long id);

    void remove(Long id);
}
