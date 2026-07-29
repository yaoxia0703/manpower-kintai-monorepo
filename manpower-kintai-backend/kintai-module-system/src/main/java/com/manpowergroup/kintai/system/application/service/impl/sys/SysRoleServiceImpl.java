package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.RoleCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEmployeeRoleRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleMenuRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRolePermissionRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {


    private final SysRoleRepository roleRepository;
    private final SysRoleMenuRepository roleMenuRepository;
    private final SysEmployeeRoleRepository employeeRoleRepository;
    private final SysRolePermissionRepository rolePermissionRepository;

    @Override

    public SysRole getById(Long id) {
        return requireRole(id);
    }

    private SysRole requireRole(Long id) {
        SysRole role = roleRepository.findById(id);
        if (role == null) throw new BizException(SystemErrorCode.ROLE_NOT_FOUND);
        return role;
    }

    @Override
    public PageResult<SysRole> page(Long companyId, PageRequest request) {
        return roleRepository.pageByCompany(companyId, request.page(), request.size());
    }

    @Override
    public List<SysRole> listByCompany(Long companyId) {
        return roleRepository.listByCompany(companyId);
    }

    @Override
    @Transactional
    public SysRole create(RoleCreateCommand command) {
        if (roleRepository.existsByCompanyAndCode(command.companyId(), command.code()))
            throw new BizException(SystemErrorCode.ROLE_CODE_DUPLICATE);
        SysRole role = SysRole.create(
            command.companyId(), command.code(), command.name(), command.remark(), command.sort());
        roleRepository.save(role);
        return role;
    }

    @Override
    @Transactional
    public SysRole update(Long id, RoleUpdateCommand command) {
        SysRole existing = requireRole(id);

        if (roleRepository.existsByCompanyAndCodeExcludingId(command.companyId(), id, command.code()))
            throw new BizException(SystemErrorCode.ROLE_CODE_DUPLICATE);
        existing.updateEditableFields(
            command.companyId(), command.code(), command.name(), command.remark(), command.sort());
        roleRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysRole role = requireRole(id);
        role.enable();
        roleRepository.updateById(role);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysRole role = requireRole(id);
        role.disable();
        roleRepository.updateById(role);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        requireRole(id);
        if (employeeRoleRepository.existsById(id)) throw new BizException(SystemErrorCode.ROLE_ASSIGNED_TO_EMPLOYEE);
        roleMenuRepository.deleteByRoleId(id);
        rolePermissionRepository.deleteByRoleId(id);
        roleRepository.deleteById(id);;
    }

    enum SystemErrorCode implements BaseErrorCode {
        ROLE_NOT_FOUND(404, "error.role.not_found"),
        ROLE_CODE_DUPLICATE(409, "error.role.code_duplicate"),
        ROLE_ASSIGNED_TO_EMPLOYEE(409, "error.role.assigned_to_employee");

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
