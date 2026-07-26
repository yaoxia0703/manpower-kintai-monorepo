package com.manpowergroup.kintai.employee.application.service.impl.emp;

import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionUpdateCommand;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.employee.domain.repository.emp.EmpEmployeePositionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// 社員職位サービス実装（アプリケーション層）
@Service
public class EmpEmployeePositionServiceImpl implements EmpEmployeePositionService {

    private final EmpEmployeePositionRepository empEmployeePositionRepository;

    public EmpEmployeePositionServiceImpl(EmpEmployeePositionRepository empEmployeePositionRepository) {
        this.empEmployeePositionRepository = empEmployeePositionRepository;
    }

    @Override
    public EmpEmployeePosition getById(Long id) {
        return requirePosition(id);
    }

    private EmpEmployeePosition requirePosition(Long id) {
        EmpEmployeePosition pos = empEmployeePositionRepository.getById(id);
        if (pos == null) throw new BizException(SystemErrorCode.POSITION_NOT_FOUND);
        return pos;
    }

    @Override
    public List<EmpEmployeePosition> listActiveByEmployee(Long employeeId) {
        return empEmployeePositionRepository.getByEmployeeIdAndStartDateAndEndDate(employeeId);
    }

    @Override
    public List<EmpEmployeePosition> listAllByEmployee(Long employeeId) {
        return empEmployeePositionRepository.getByEmployeeId(employeeId);
    }

    @Override
    public EmpEmployeePosition getPrimaryByEmployee(Long employeeId) {
        EmpEmployeePosition pos = empEmployeePositionRepository.getPrimaryByEmployee(employeeId);
        if (pos == null) throw new BizException(SystemErrorCode.POSITION_NOT_FOUND);
        return pos;
    }

    @Override
    @Transactional
    public EmpEmployeePosition create(EmployeePositionCreateCommand command) {
        EmpEmployeePosition position = EmpEmployeePosition.assign(
            command.employeeId(),
            command.companyId(),
            command.nodeId(),
            command.gradeId(),
            command.isPrimary(),
            command.startDate(),
            command.endDate(),
            command.status());
        empEmployeePositionRepository.save(position);
        return position;
    }

    @Override
    @Transactional
    public EmpEmployeePosition update(Long id, EmployeePositionUpdateCommand command) {
        EmpEmployeePosition existing = requirePosition(id);
        existing.updateAssignment(
            command.nodeId(),
            command.gradeId(),
            command.isPrimary(),
            command.startDate(),
            command.endDate());
        empEmployeePositionRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void terminate(Long id) {
        EmpEmployeePosition position = requirePosition(id);
        // 離任日を本日に設定して終了
        position.terminate(LocalDate.now());
        empEmployeePositionRepository.updateById(position);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        requirePosition(id);
        empEmployeePositionRepository.deleteById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        POSITION_NOT_FOUND(404, "error.position.not_found");

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

