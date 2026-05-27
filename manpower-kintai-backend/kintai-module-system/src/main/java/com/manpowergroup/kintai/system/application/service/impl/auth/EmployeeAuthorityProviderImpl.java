package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.framework.security.authority.EmployeeAuthorityProvider;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeAuthorityProviderImpl implements EmployeeAuthorityProvider {

    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysPermissionMapper permissionMapper;

    @Override
    public List<String> loadPermissionCodes(Long employeeId) {
        LocalDate today = LocalDate.now();

        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId)
                        .and(q -> q.isNull(SysEmployeeRole::getStartDate)
                                .or()
                                .le(SysEmployeeRole::getStartDate, today))
                        .and(q -> q.isNull(SysEmployeeRole::getEndDate)
                                .or()
                                .ge(SysEmployeeRole::getEndDate, today)))
                .stream()
                .map(SysEmployeeRole::getRoleId)
                .distinct()
                .toList();

        if (roleIds.isEmpty()) return Collections.emptyList();

        List<Long> permissionIds = rolePermissionMapper.selectList(Wrappers.<SysRolePermission>lambdaQuery()
                        .in(SysRolePermission::getRoleId, roleIds))
                .stream()
                .map(SysRolePermission::getPermissionId)
                .distinct()
                .toList();

        if (permissionIds.isEmpty()) return Collections.emptyList();

        return permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
                        .in(SysPermission::getId, permissionIds)
                        .eq(SysPermission::getStatus, Status.ENABLED))
                .stream()
                .map(SysPermission::getCode)
                .distinct()
                .toList();
    }
}
