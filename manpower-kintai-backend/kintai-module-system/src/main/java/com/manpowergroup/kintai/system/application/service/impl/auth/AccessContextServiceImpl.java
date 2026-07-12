package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.dto.auth.AccessContext;
import com.manpowergroup.kintai.common.dto.auth.CurrentUserResponse;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import com.manpowergroup.kintai.system.application.service.auth.AccessContextService;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccessContextServiceImpl implements AccessContextService {

    private static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";

    private final EmpEmployeeService employeeService;
    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRoleMapper roleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysPermissionMapper permissionMapper;
    private final SysMenuService menuService;

    @Override
    public AccessContext load(Long employeeId, Long accountId) {
        EmpEmployee employee = employeeService.findById(employeeId)
                .orElseThrow(() -> new BizException(ErrorCode.UNAUTHORIZED));

        List<Long> roleIds = loadActiveRoleIds(employeeId);
        List<String> roles = loadRoleCodes(roleIds);
        List<String> permissions = roles.contains(SUPER_ADMIN_ROLE)
                ? List.of("*")
                : loadPermissionCodes(roleIds);
        List<String> authorities = buildAuthorities(roles, permissions);
        List<CurrentUserResponse.MenuItem> menus = loadMenus(employeeId);

        return AccessContext.builder()
                .user(CurrentUserResponse.UserProfile.builder()
                        .employeeId(employee.getId())
                        .accountId(accountId)
                        .companyId(employee.getCompanyId())
                        .employeeCode(employee.getEmployeeCode())
                        .displayName(employee.getLastName() + " " + employee.getFirstName())
                        .email(employee.getEmail())
                        .build())
                .roles(roles)
                .permissions(permissions)
                .authorities(authorities)
                .menus(menus)
                .build();
    }

    private List<Long> loadActiveRoleIds(Long employeeId) {
        LocalDate today = LocalDate.now();
        return employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId))
                .stream()
                .filter(assignment -> assignment.isEffectiveOn(today))
                .map(SysEmployeeRole::getRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private List<String> loadRoleCodes(List<Long> roleIds) {
        if (roleIds.isEmpty()) return Collections.emptyList();
        return roleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                        .in(SysRole::getId, roleIds)
                        .eq(SysRole::getStatus, Status.ENABLED))
                .stream()
                .map(SysRole::getCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private List<String> loadPermissionCodes(List<Long> roleIds) {
        if (roleIds.isEmpty()) return Collections.emptyList();

        List<Long> permissionIds = rolePermissionMapper.selectList(Wrappers.<SysRolePermission>lambdaQuery()
                        .in(SysRolePermission::getRoleId, roleIds))
                .stream()
                .map(SysRolePermission::getPermissionId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (permissionIds.isEmpty()) return Collections.emptyList();

        return permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
                        .in(SysPermission::getId, permissionIds)
                        .eq(SysPermission::getStatus, Status.ENABLED))
                .stream()
                .map(SysPermission::getCode)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private List<String> buildAuthorities(List<String> roles, List<String> permissions) {
        List<String> roleAuthorities = roles.stream()
                .filter(Objects::nonNull)
                .map(role -> "ROLE_" + role)
                .toList();
        return java.util.stream.Stream.concat(roleAuthorities.stream(), permissions.stream())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .toList();
    }

    private List<CurrentUserResponse.MenuItem> loadMenus(Long employeeId) {
        return menuService.listByEmployeeId(employeeId)
                .stream()
                .filter(menu -> menu.getStatus() == Status.ENABLED)
                .filter(menu -> menu.getVisible() == null || menu.getVisible() == 1)
                .map(this::toMenuItem)
                .toList();
    }

    private CurrentUserResponse.MenuItem toMenuItem(SysMenu menu) {
        return CurrentUserResponse.MenuItem.builder()
                .id(menu.getId())
                .parentId(menu.getParentId())
                .name(menu.getName())
                .code(menu.getCode())
                .path(menu.getPath())
                .component(menu.getComponent())
                .icon(menu.getIcon())
                .type(menu.getType())
                .sort(menu.getSort())
                .visible(menu.getVisible())
                .build();
    }
}
