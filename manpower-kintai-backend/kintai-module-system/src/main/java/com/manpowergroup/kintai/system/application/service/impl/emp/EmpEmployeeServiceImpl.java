package com.manpowergroup.kintai.system.application.service.impl.emp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.repository.emp.EmpEmployeeRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 社員マスタサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class EmpEmployeeServiceImpl extends ServiceImpl<EmpEmployeeMapper, EmpEmployee>
        implements EmpEmployeeService {

    private final EmpEmployeeRepository repository;

    @Override
    public EmpEmployee getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BizException(SystemErrorCode.EMPLOYEE_NOT_FOUND));
    }

    @Override
    public PageResult<EmpEmployee> pageByCompany(Long companyId, PageRequest request) {
        return repository.findPageByCompanyId(companyId, request.page(), request.size());
    }

    @Override
    public PageResult<EmpEmployee> searchByName(Long companyId, String keyword, PageRequest request) {
        return repository.searchByName(companyId, keyword, request.page(), request.size());
    }

    @Override
    @Transactional
    public EmpEmployee create(EmpEmployee employee) {
        if (repository.existsByEmail(employee.getEmail(), null)) {
            throw new BizException(SystemErrorCode.EMPLOYEE_EMAIL_DUPLICATE);
        }
        return repository.save(employee);
    }

    @Override
    @Transactional
    public EmpEmployee update(Long id, EmpEmployee employee) {
        EmpEmployee existing = getById(id);
        if (repository.existsByEmail(employee.getEmail(), id)) {
            throw new BizException(SystemErrorCode.EMPLOYEE_EMAIL_DUPLICATE);
        }
        existing.setLastName(employee.getLastName())
                .setFirstName(employee.getFirstName())
                .setLastNameKana(employee.getLastNameKana())
                .setFirstNameKana(employee.getFirstNameKana())
                .setEmail(employee.getEmail())
                .setPhone(employee.getPhone())
                .setGender(employee.getGender())
                .setBirthDate(employee.getBirthDate());
        return repository.update(existing);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        EmpEmployee employee = getById(id);
        employee.setStatus(Status.ENABLED);
        repository.update(employee);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        EmpEmployee employee = getById(id);
        employee.setStatus(Status.DISABLED);
        repository.update(employee);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        repository.removeById(id);
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

