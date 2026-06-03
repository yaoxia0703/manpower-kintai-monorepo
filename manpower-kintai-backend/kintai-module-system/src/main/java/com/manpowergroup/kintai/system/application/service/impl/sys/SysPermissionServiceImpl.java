package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.PermissionCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.PermissionUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission>
        implements SysPermissionService {

    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;

    @Override
    public SysPermission getById(Long id) {
        return requirePermission(id);
    }

    private SysPermission requirePermission(Long id) {
        SysPermission permission = super.getById(id);
        if (permission == null) throw new BizException(SystemErrorCode.PERMISSION_NOT_FOUND);
        return permission;
    }

    @Override
    public PageResult<SysPermission> page(PageRequest request) {
        Page<SysPermission> p = new Page<>(request.page(), request.size());
        page(p, lambdaQuery().orderByAsc(SysPermission::getSort).getWrapper());
        return PageResult.of(p);
    }

    @Override
    public List<SysPermission> listByMenu(Long menuId) {
        return lambdaQuery()
                .eq(SysPermission::getMenuId, menuId)
                .orderByAsc(SysPermission::getSort)
                .list();
    }

    @Override
    public List<SysPermission> listByEmployeeId(Long employeeId) {
        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId)
                        .and(w -> w.isNull(SysEmployeeRole::getStartDate)
                                .or().le(SysEmployeeRole::getStartDate, LocalDate.now()))
                        .and(w -> w.isNull(SysEmployeeRole::getEndDate)
                                .or().ge(SysEmployeeRole::getEndDate, LocalDate.now())))
                .stream().map(SysEmployeeRole::getRoleId).collect(Collectors.toList());
        if (roleIds.isEmpty()) return Collections.emptyList();

        List<Long> permissionIds = rolePermissionMapper.selectList(Wrappers.<SysRolePermission>lambdaQuery()
                        .in(SysRolePermission::getRoleId, roleIds))
                .stream().map(SysRolePermission::getPermissionId).distinct().collect(Collectors.toList());
        if (permissionIds.isEmpty()) return Collections.emptyList();

        return lambdaQuery()
                .in(SysPermission::getId, permissionIds)
                .orderByAsc(SysPermission::getSort)
                .list();
    }

    @Override
    @Transactional
    public SysPermission create(PermissionCreateCommand command) {
        ensureCodeUnique(command.code(), null);
        SysPermission permission = new SysPermission()
                .setMenuId(command.menuId())
                .setCode(command.code())
                .setName(command.name())
                .setMethod(command.method())
                .setPath(command.path())
                .setRemark(command.remark())
                .setSort(command.sort())
                .setStatus(Status.ENABLED);
        save(permission);
        return permission;
    }

    @Override
    @Transactional
    public SysPermission update(Long id, PermissionUpdateCommand command) {
        SysPermission existing = requirePermission(id);
        ensureCodeUnique(command.code(), id);
        existing.setCode(command.code())
                .setName(command.name())
                .setMethod(command.method())
                .setPath(command.path())
                .setRemark(command.remark())
                .setMenuId(command.menuId())
                .setSort(command.sort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysPermission permission = requirePermission(id);
        permission.enable();
        updateById(permission);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysPermission permission = requirePermission(id);
        permission.disable();
        updateById(permission);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        requirePermission(id);
        boolean assignedToRole = rolePermissionMapper.selectCount(Wrappers.<SysRolePermission>lambdaQuery()
                .eq(SysRolePermission::getPermissionId, id)) > 0;
        if (assignedToRole) throw new BizException(SystemErrorCode.PERMISSION_ASSIGNED_TO_ROLE);
        removeById(id);
    }

    private void ensureCodeUnique(String code, Long currentId) {
        boolean exists = lambdaQuery()
                .eq(SysPermission::getCode, code)
                .ne(currentId != null, SysPermission::getId, currentId)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.PERMISSION_CODE_DUPLICATE);
    }

    enum SystemErrorCode implements BaseErrorCode {
        PERMISSION_NOT_FOUND(404, "error.permission.not_found"),
        PERMISSION_CODE_DUPLICATE(409, "error.permission.code_duplicate"),
        PERMISSION_ASSIGNED_TO_ROLE(409, "error.permission.assigned_to_role");

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
