package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.service.sys.SysEmployeeRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 社員ロール関連サービス実装（アプリケーション層）
@Service
public class SysEmployeeRoleServiceImpl extends ServiceImpl<SysEmployeeRoleMapper, SysEmployeeRole>
        implements SysEmployeeRoleService {

    @Override
    public SysEmployeeRole getById(Long id) {
        SysEmployeeRole er = super.getById(id);
        if (er == null) throw new BizException(SystemErrorCode.EMPLOYEE_ROLE_NOT_FOUND);
        return er;
    }

    @Override
    public List<SysEmployeeRole> listActiveByEmployee(Long employeeId) {
        return lambdaQuery()
                .eq(SysEmployeeRole::getEmployeeId, employeeId)
                .le(SysEmployeeRole::getStartDate, LocalDate.now())
                .and(w -> w.isNull(SysEmployeeRole::getEndDate)
                        .or().ge(SysEmployeeRole::getEndDate, LocalDate.now()))
                .list();
    }

    enum SystemErrorCode implements BaseErrorCode {
        EMPLOYEE_ROLE_NOT_FOUND(404, "error.employee_role.not_found");

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

