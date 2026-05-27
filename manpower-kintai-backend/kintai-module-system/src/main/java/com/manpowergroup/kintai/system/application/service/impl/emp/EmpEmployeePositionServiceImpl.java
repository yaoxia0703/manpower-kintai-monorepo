package com.manpowergroup.kintai.system.application.service.impl.emp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeePositionMapper;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeePositionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// 社員職位サービス実装（アプリケーション層）
@Service
public class EmpEmployeePositionServiceImpl extends ServiceImpl<EmpEmployeePositionMapper, EmpEmployeePosition>
        implements EmpEmployeePositionService {

    @Override
    public EmpEmployeePosition getById(Long id) {
        EmpEmployeePosition pos = super.getById(id);
        if (pos == null) throw new BizException(SystemErrorCode.POSITION_NOT_FOUND);
        return pos;
    }

    @Override
    public List<EmpEmployeePosition> listActiveByEmployee(Long employeeId) {
        return lambdaQuery()
                .eq(EmpEmployeePosition::getEmployeeId, employeeId)
                .le(EmpEmployeePosition::getStartDate, LocalDate.now())
                .and(w -> w.isNull(EmpEmployeePosition::getEndDate)
                        .or().ge(EmpEmployeePosition::getEndDate, LocalDate.now()))
                .list();
    }

    @Override
    public List<EmpEmployeePosition> listAllByEmployee(Long employeeId) {
        return lambdaQuery()
                .eq(EmpEmployeePosition::getEmployeeId, employeeId)
                .orderByDesc(EmpEmployeePosition::getStartDate)
                .list();
    }

    @Override
    public EmpEmployeePosition getPrimaryByEmployee(Long employeeId) {
        EmpEmployeePosition pos = lambdaQuery()
                .eq(EmpEmployeePosition::getEmployeeId, employeeId)
                .eq(EmpEmployeePosition::getIsPrimary, 1)
                .le(EmpEmployeePosition::getStartDate, LocalDate.now())
                .and(w -> w.isNull(EmpEmployeePosition::getEndDate)
                        .or().ge(EmpEmployeePosition::getEndDate, LocalDate.now()))
                .one();
        if (pos == null) throw new BizException(SystemErrorCode.POSITION_NOT_FOUND);
        return pos;
    }

    @Override
    @Transactional
    public EmpEmployeePosition create(EmpEmployeePosition position) {
        save(position);
        return position;
    }

    @Override
    @Transactional
    public EmpEmployeePosition update(Long id, EmpEmployeePosition position) {
        EmpEmployeePosition existing = getById(id);
        existing.setNodeId(position.getNodeId())
                .setGradeId(position.getGradeId())
                .setIsPrimary(position.getIsPrimary())
                .setStartDate(position.getStartDate())
                .setEndDate(position.getEndDate());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void terminate(Long id) {
        EmpEmployeePosition position = getById(id);
        // 離任日を本日に設定して終了
        position.setEndDate(LocalDate.now());
        updateById(position);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        POSITION_NOT_FOUND(404, "error.position.not_found");

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

