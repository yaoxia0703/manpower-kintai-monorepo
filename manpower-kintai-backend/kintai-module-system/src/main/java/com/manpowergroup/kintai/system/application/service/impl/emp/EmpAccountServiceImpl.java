package com.manpowergroup.kintai.system.application.service.impl.emp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.system.application.command.emp.AccountUpdateCommand;
import com.manpowergroup.kintai.system.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 社員アカウントサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class EmpAccountServiceImpl extends ServiceImpl<EmpAccountMapper, EmpAccount>
        implements EmpAccountService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public EmpAccount getById(Long id) {
        return requireAccount(id);
    }

    @Override
    public EmpAccount getByEmployeeId(Long employeeId) {
        return requireAccountByEmployeeId(employeeId);
    }

    private EmpAccount requireAccount(Long id) {
        EmpAccount account = super.getById(id);
        if (account == null) throw new BizException(SystemErrorCode.ACCOUNT_NOT_FOUND);
        return account;
    }

    private EmpAccount requireAccountByEmployeeId(Long employeeId) {
        EmpAccount account = lambdaQuery().eq(EmpAccount::getEmployeeId, employeeId).one();
        if (account == null) throw new BizException(SystemErrorCode.ACCOUNT_NOT_FOUND);
        return account;
    }

    @Override
    @Transactional
    public EmpAccount create(AccountCreateCommand command) {
        boolean exists = lambdaQuery().eq(EmpAccount::getUsername, command.username()).count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ACCOUNT_USERNAME_DUPLICATE);
        EmpAccount account = new EmpAccount()
                .setEmployeeId(command.employeeId())
                .setUsername(command.username())
                .setPassword(passwordEncoder.encode(command.password()))
                .setStatus(Status.ENABLED);
        save(account);
        return account;
    }

    @Override
    @Transactional
    public EmpAccount update(Long id, AccountUpdateCommand command) {
        EmpAccount existing = getById(id);
        boolean exists = lambdaQuery()
                .eq(EmpAccount::getUsername, command.username())
                .ne(EmpAccount::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ACCOUNT_USERNAME_DUPLICATE);
        existing.changeUsername(command.username());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword) {
        EmpAccount account = getById(id);
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new BizException(SystemErrorCode.ACCOUNT_PASSWORD_MISMATCH);
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        updateById(account);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        EmpAccount account = getById(id);
        account.enable();
        updateById(account);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        EmpAccount account = getById(id);
        account.disable();
        updateById(account);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        ACCOUNT_NOT_FOUND(404, "error.account.not_found"),
        ACCOUNT_USERNAME_DUPLICATE(409, "error.account.username_duplicate"),
        ACCOUNT_PASSWORD_MISMATCH(400, "error.account.password_mismatch");

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

