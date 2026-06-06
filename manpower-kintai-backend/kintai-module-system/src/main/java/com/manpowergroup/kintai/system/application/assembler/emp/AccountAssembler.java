package com.manpowergroup.kintai.system.application.assembler.emp;

import com.manpowergroup.kintai.system.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.system.application.command.emp.AccountUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.emp.response.AccountResponse;
import com.manpowergroup.kintai.system.application.dto.emp.request.AccountCreateRequest;
import com.manpowergroup.kintai.system.application.dto.emp.request.AccountUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;

public final class AccountAssembler {

    private AccountAssembler() {
    }

    public static AccountCreateCommand toCommand(AccountCreateRequest request) {
        return new AccountCreateCommand(
                request.getEmployeeId(),
                request.getUsername(),
                request.getPassword()
        );
    }

    public static AccountUpdateCommand toCommand(AccountUpdateRequest request) {
        return new AccountUpdateCommand(request.getUsername());
    }

    public static AccountResponse toResponse(EmpAccount account) {
        return AccountResponse.from(account);
    }
}
