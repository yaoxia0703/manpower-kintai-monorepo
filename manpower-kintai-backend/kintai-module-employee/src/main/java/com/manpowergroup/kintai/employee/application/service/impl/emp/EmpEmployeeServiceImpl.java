package com.manpowergroup.kintai.employee.application.service.impl.emp;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeUpdateCommand;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.employee.domain.repository.emp.EmpEmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// 社員マスタサービス実装（アプリケーション層）
@Service
public class EmpEmployeeServiceImpl implements EmpEmployeeService {

    private final EmpEmployeeRepository empEmployeeRepository;

    public EmpEmployeeServiceImpl(EmpEmployeeRepository empEmployeeRepository) {
        this.empEmployeeRepository = empEmployeeRepository;
    }

    @Override
    public Optional<EmpEmployee> findById(Long id) {
        return Optional.ofNullable(empEmployeeRepository.getById(id));
    }

    @Override
    public Optional<EmpEmployee> findByEmail(String email) {
        return Optional.ofNullable(empEmployeeRepository.findByEmail(email));
    }

    @Override
    public EmpEmployee getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new BizException(SystemErrorCode.EMPLOYEE_NOT_FOUND));
    }

    @Override
    public PageResult<EmpEmployee> pageByCompany(Long companyId, PageRequest request) {
        return empEmployeeRepository.findPageByCompany(companyId, request);
    }

    @Override
    public PageResult<EmpEmployee> searchByName(Long companyId, String keyword, PageRequest request) {
        return empEmployeeRepository.findPageByCompanyAndKeyword(companyId, keyword, request);
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
        empEmployeeRepository.save(employee);
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
        empEmployeeRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        EmpEmployee employee = getById(id);
        employee.enable();
        empEmployeeRepository.updateById(employee);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        EmpEmployee employee = getById(id);
        employee.disable();
        empEmployeeRepository.updateById(employee);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        empEmployeeRepository.deleteById(id);
    }

    private boolean existsByEmail(String email, Long excludeId) {
        return empEmployeeRepository.existsByEmail(email, excludeId);
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

        @Override
        public int code() {
            return code;
        }

        @Override
        public String messageKey() {
            return messageKey;
        }
    }
}
