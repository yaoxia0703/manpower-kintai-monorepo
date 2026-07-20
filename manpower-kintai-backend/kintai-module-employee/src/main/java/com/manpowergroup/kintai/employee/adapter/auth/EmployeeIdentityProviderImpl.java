package com.manpowergroup.kintai.employee.adapter.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmpAccountMapper;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.system.application.port.auth.EmployeeIdentityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmployeeIdentityProviderImpl implements EmployeeIdentityProvider {

    private final EmpEmployeeMapper employeeMapper;
    private final EmpAccountMapper accountMapper;

    @Override
    public Optional<LoginIdentity> findLoginIdentityByEmail(String email) {
        EmpEmployee employee = employeeMapper.selectOne(Wrappers.<EmpEmployee>lambdaQuery()
                .eq(EmpEmployee::getEmail, email));
        if (employee == null) {
            return Optional.empty();
        }
        EmpAccount account = accountMapper.selectOne(Wrappers.<EmpAccount>lambdaQuery()
                .eq(EmpAccount::getEmployeeId, employee.getId()));
        if (account == null) {
            return Optional.empty();
        }
        return Optional.of(new LoginIdentity(
                employee.getId(),
                account.getId(),
                account.getPassword(),
                employee.getStatus(),
                account.getStatus(),
                employee.getLastName() + " " + employee.getFirstName(),
                employee.getEmail()));
    }

    @Override
    public Optional<EmployeeProfile> findEmployeeProfile(Long employeeId) {
        return Optional.ofNullable(employeeMapper.selectById(employeeId))
                .map(employee -> new EmployeeProfile(
                        employee.getId(),
                        employee.getCompanyId(),
                        employee.getEmployeeCode(),
                        employee.getLastName() + " " + employee.getFirstName(),
                        employee.getEmail()));
    }

    @Override
    @Transactional
    public void recordSuccessfulLogin(Long accountId, LocalDateTime loggedInAt) {
        EmpAccount account = accountMapper.selectById(accountId);
        if (account != null) {
            account.recordLogin(loggedInAt);
            accountMapper.updateById(account);
        }
    }
}
