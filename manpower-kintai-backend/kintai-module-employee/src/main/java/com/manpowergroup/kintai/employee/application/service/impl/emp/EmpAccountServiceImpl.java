package com.manpowergroup.kintai.employee.application.service.impl.emp;

import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.employee.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.AccountUpdateCommand;
import com.manpowergroup.kintai.employee.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.employee.domain.repository.emp.EmpAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 社員アカウントサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class EmpAccountServiceImpl implements EmpAccountService {

    private final PasswordEncoder passwordEncoder;

    private final EmpAccountRepository empAccountRepository;

    @Override
    public EmpAccount getById(Long id) {
        return requireAccount(id);
    }

    @Override
    public EmpAccount getByEmployeeId(Long employeeId) {
        return requireAccountByEmployeeId(employeeId);
    }

    private EmpAccount requireAccount(Long id) {
        EmpAccount account = empAccountRepository.getById(id);
        if (account == null) throw new BizException(SystemErrorCode.ACCOUNT_NOT_FOUND);
        return account;
    }

    private EmpAccount requireAccountByEmployeeId(Long employeeId) {
        EmpAccount account = empAccountRepository.getByEmployeeId(employeeId);
        if (account == null) throw new BizException(SystemErrorCode.ACCOUNT_NOT_FOUND);
        return account;
    }

    @Transactional
    @Override
    public EmpAccount create(AccountCreateCommand command) {
        boolean exists = empAccountRepository.countByUsername(command.username()) > 0;
        if (exists) throw new BizException(SystemErrorCode.ACCOUNT_USERNAME_DUPLICATE);
        EmpAccount account = EmpAccount.register(
                command.employeeId(),
                command.username(),
                command.password(),
                passwordEncoder
        );
        empAccountRepository.save(account);
        return account;
    }

    @Override
    @Transactional
    public EmpAccount update(Long id, AccountUpdateCommand command) {
        EmpAccount existing = getById(id);
        boolean exists = empAccountRepository.existsByUsernameExcludingId(command.username(), id);
        if (exists) throw new BizException(SystemErrorCode.ACCOUNT_USERNAME_DUPLICATE);
        existing.changeUsername(command.username());
        empAccountRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        EmpAccount account = getById(id);
        account.changePassword(oldPassword, newPassword, passwordEncoder);
        empAccountRepository.updateById(account);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        EmpAccount account = getById(id);
        account.enable();
        empAccountRepository.updateById(account);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        EmpAccount account = getById(id);
        account.disable();
        empAccountRepository.updateById(account);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        empAccountRepository.deleteById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        ACCOUNT_NOT_FOUND(404, "error.account.not_found"),
        ACCOUNT_USERNAME_DUPLICATE(409, "error.account.username_duplicate");

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
