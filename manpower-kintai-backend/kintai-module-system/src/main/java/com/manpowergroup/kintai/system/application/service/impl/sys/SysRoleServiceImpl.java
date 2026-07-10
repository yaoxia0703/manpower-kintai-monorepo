package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.RoleCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysEmployeeRoleMapper employeeRoleMapper;

    @Override
    public SysRole getById(Long id) {
        return requireRole(id);
    }

    private SysRole requireRole(Long id) {
        SysRole role = super.getById(id);
        if (role == null) throw new BizException(SystemErrorCode.ROLE_NOT_FOUND);
        return role;
    }

    @Override
    public PageResult<SysRole> page(Long companyId, PageRequest request) {
        Page<SysRole> p = new Page<>(request.page(), request.size());
        page(p, lambdaQuery()
                .eq(companyId != null, SysRole::getCompanyId, companyId)
                .orderByAsc(SysRole::getSort)
                .getWrapper());
        return PageResult.of(p);
    }

    @Override
    public List<SysRole> listByCompany(Long companyId) {
        return lambdaQuery()
                .eq(companyId != null, SysRole::getCompanyId, companyId)
                .orderByAsc(SysRole::getSort)
                .list();
    }

    @Override
    @Transactional
    public SysRole create(RoleCreateCommand command) {
        boolean exists = lambdaQuery()
                .eq(command.companyId() != null, SysRole::getCompanyId, command.companyId())
                .isNull(command.companyId() == null, SysRole::getCompanyId)
                .eq(SysRole::getCode, command.code())
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ROLE_CODE_DUPLICATE);
        SysRole role = new SysRole()
                .setCompanyId(command.companyId())
                .setCode(command.code())
                .setName(command.name())
                .setRemark(command.remark())
                .setSort(command.sort())
                .setStatus(Status.ENABLED);
        save(role);
        return role;
    }

    @Override
    @Transactional
    public SysRole update(Long id, RoleUpdateCommand command) {
        SysRole existing = requireRole(id);
        boolean exists = lambdaQuery()
                .eq(command.companyId() != null, SysRole::getCompanyId, command.companyId())
                .isNull(command.companyId() == null, SysRole::getCompanyId)
                .eq(SysRole::getCode, command.code())
                .ne(SysRole::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ROLE_CODE_DUPLICATE);
        existing.setCompanyId(command.companyId())
                .setName(command.name())
                .setCode(command.code())
                .setRemark(command.remark())
                .setSort(command.sort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysRole role = requireRole(id);
        role.enable();
        updateById(role);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysRole role = requireRole(id);
        role.disable();
        updateById(role);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        requireRole(id);
        boolean assignedToEmployee = employeeRoleMapper.selectCount(Wrappers.<SysEmployeeRole>lambdaQuery()
                .eq(SysEmployeeRole::getRoleId, id)) > 0;
        if (assignedToEmployee) throw new BizException(SystemErrorCode.ROLE_ASSIGNED_TO_EMPLOYEE);
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, id));
        rolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId, id));
        removeById(id);
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

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}
