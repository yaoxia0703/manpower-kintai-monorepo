package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// 権限マスタサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission>
        implements SysPermissionService {

    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;

    @Override
    public SysPermission getById(Long id) {
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
        // 有効期間内のロールIDを取得
        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId)
                        .le(SysEmployeeRole::getStartDate, LocalDate.now())
                        .and(w -> w.isNull(SysEmployeeRole::getEndDate)
                                .or().ge(SysEmployeeRole::getEndDate, LocalDate.now())))
                .stream().map(SysEmployeeRole::getRoleId).collect(Collectors.toList());
        if (roleIds.isEmpty()) return Collections.emptyList();
        // ロールに紐づく権限IDを取得
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
    public SysPermission create(SysPermission permission) {
        save(permission);
        return permission;
    }

    @Override
    @Transactional
    public SysPermission update(Long id, SysPermission permission) {
        SysPermission existing = getById(id);
        existing.setCode(permission.getCode())
                .setName(permission.getName())
                .setMethod(permission.getMethod())
                .setPath(permission.getPath())
                .setRemark(permission.getRemark())
                .setMenuId(permission.getMenuId())
                .setSort(permission.getSort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysPermission permission = getById(id);
        permission.setStatus(Status.ENABLED);
        updateById(permission);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysPermission permission = getById(id);
        permission.setStatus(Status.DISABLED);
        updateById(permission);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        PERMISSION_NOT_FOUND(404, "error.permission.not_found");

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

