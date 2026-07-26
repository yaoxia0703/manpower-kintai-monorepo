package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.service.sys.SysEmployeeRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEmployeeRoleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 社員ロール関連サービス実装（アプリケーション層）
@Service
public class SysEmployeeRoleServiceImpl implements SysEmployeeRoleService {

    private final SysEmployeeRoleRepository sysEmployeeRoleRepository;

    public SysEmployeeRoleServiceImpl(SysEmployeeRoleRepository sysEmployeeRoleRepository) {
        this.sysEmployeeRoleRepository = sysEmployeeRoleRepository;
    }

    @Override
    public SysEmployeeRole getById(Long id) {
        SysEmployeeRole er = sysEmployeeRoleRepository.findById(id);
        if (er == null) throw new BizException(SystemErrorCode.EMPLOYEE_ROLE_NOT_FOUND);
        return er;
    }

    @Override
    public List<SysEmployeeRole> listActiveByEmployee(Long employeeId) {
        LocalDate today = LocalDate.now();
        return sysEmployeeRoleRepository.listByEmployee(employeeId)
            .stream()
            .filter(assignment -> assignment.isEffectiveOn(today))
            .toList();
    }

    enum SystemErrorCode implements BaseErrorCode {
        EMPLOYEE_ROLE_NOT_FOUND(404, "error.employee_role.not_found");

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

