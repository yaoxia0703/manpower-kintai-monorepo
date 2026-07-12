package com.manpowergroup.kintai.system.application.service.impl.emp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.system.application.command.emp.EmployeeUpdateCommand;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

// 社員マスタサービス実装（アプリケーション層）
@Service
public class EmpEmployeeServiceImpl extends ServiceImpl<EmpEmployeeMapper, EmpEmployee>
        implements EmpEmployeeService {

    @Override
    public Optional<EmpEmployee> findById(Long id) {
        return Optional.ofNullable(super.getById(id));
    }

    @Override
    public Optional<EmpEmployee> findByEmail(String email) {
        return Optional.ofNullable(lambdaQuery()
                .eq(EmpEmployee::getEmail, email)
                .one());
    }

    @Override
    public EmpEmployee getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new BizException(SystemErrorCode.EMPLOYEE_NOT_FOUND));
    }

    @Override
    public PageResult<EmpEmployee> pageByCompany(Long companyId, PageRequest request) {
        Page<EmpEmployee> page = new Page<>(request.page(), request.size());
        page(page, lambdaQuery()
                .eq(EmpEmployee::getCompanyId, companyId)
                .orderByAsc(EmpEmployee::getEmployeeCode)
                .getWrapper());
        return PageResult.of(page);
    }

    @Override
    public PageResult<EmpEmployee> searchByName(Long companyId, String keyword, PageRequest request) {
        Page<EmpEmployee> page = new Page<>(request.page(), request.size());
        LambdaQueryWrapper<EmpEmployee> wrapper = Wrappers.<EmpEmployee>lambdaQuery()
                .eq(EmpEmployee::getCompanyId, companyId);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(q -> q
                    .like(EmpEmployee::getEmployeeCode, keyword)
                    .or()
                    .like(EmpEmployee::getLastName, keyword)
                    .or()
                    .like(EmpEmployee::getFirstName, keyword)
                    .or()
                    .like(EmpEmployee::getLastNameKana, keyword)
                    .or()
                    .like(EmpEmployee::getFirstNameKana, keyword));
        }
        wrapper.orderByAsc(EmpEmployee::getEmployeeCode);
        page(page, wrapper);
        return PageResult.of(page);
    }

    @Override
    @Transactional
    public EmpEmployee create(EmployeeCreateCommand command) {
        EmpEmployee employee = EmpEmployee.create(
                command.companyId(),
                command.employeeCode(),
                command.lastName(),
                command.firstName(),
                command.lastNameKana(),
                command.firstNameKana(),
                command.email(),
                command.phone(),
                command.gender(),
                command.birthDate(),
                command.hireDate(),
                command.leaveDate(),
                command.status());
        if (existsByEmail(employee.getEmail(), null)) {
            throw new BizException(SystemErrorCode.EMPLOYEE_EMAIL_DUPLICATE);
        }
        save(employee);
        return employee;
    }

    @Override
    @Transactional
    public EmpEmployee update(Long id, EmployeeUpdateCommand command) {
        EmpEmployee existing = getById(id);
        if (existsByEmail(command.email(), id)) {
            throw new BizException(SystemErrorCode.EMPLOYEE_EMAIL_DUPLICATE);
        }
        existing.updatePersonalInfo(
                command.lastName(),
                command.firstName(),
                command.lastNameKana(),
                command.firstNameKana(),
                command.email(),
                command.phone(),
                command.gender(),
                command.birthDate());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        EmpEmployee employee = getById(id);
        employee.enable();
        updateById(employee);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        EmpEmployee employee = getById(id);
        employee.disable();
        updateById(employee);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
    }

    private boolean existsByEmail(String email, Long excludeId) {
        return lambdaQuery()
                .eq(EmpEmployee::getEmail, email)
                .ne(excludeId != null, EmpEmployee::getId, excludeId)
                .count() > 0;
    }

    enum SystemErrorCode implements BaseErrorCode {
        EMPLOYEE_NOT_FOUND(404, "error.employee.not_found"),
        EMPLOYEE_EMAIL_DUPLICATE(409, "error.employee.email_duplicate");

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
