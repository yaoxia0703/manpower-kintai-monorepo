package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.application.assembler.sys.PermissionAssembler;
import com.manpowergroup.kintai.system.application.command.sys.RoleAuthorizationSaveCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleMenuAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.RolePermissionAssignCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.MenuResponse;
import com.manpowergroup.kintai.system.application.dto.sys.response.RoleAuthorizationResponse;
import com.manpowergroup.kintai.system.application.service.sys.RoleAuthorizationService;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.domain.model.sys.RoleAuthorization;
import com.manpowergroup.kintai.system.domain.service.sys.RoleAuthorizationDomainService;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleAuthorizationServiceImpl implements RoleAuthorizationService {

    private final SysRoleService roleService;
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysMenuMapper menuMapper;
    private final SysPermissionMapper permissionMapper;
    private final RoleAuthorizationDomainService authorizationDomainService;

    @Override
    public RoleAuthorizationResponse getAuthorization(Long roleId) {
        roleService.getById(roleId);
        List<Long> selectedMenuIds = roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                        .eq(SysRoleMenu::getRoleId, roleId))
                .stream()
                .map(SysRoleMenu::getMenuId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<Long> selectedPermissionIds = rolePermissionMapper.selectList(Wrappers.<SysRolePermission>lambdaQuery()
                        .eq(SysRolePermission::getRoleId, roleId))
                .stream()
                .map(SysRolePermission::getPermissionId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<MenuResponse> menus = menuMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                        .orderByAsc(SysMenu::getSort))
                .stream()
                .map(MenuResponse::from)
                .toList();
        List<SysPermission> permissions = permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
                .orderByAsc(SysPermission::getSort));

        return RoleAuthorizationResponse.builder()
                .menus(menus)
                .permissions(permissions.stream().map(PermissionAssembler::toResponse).toList())
                .selectedMenuIds(selectedMenuIds)
                .selectedPermissionIds(selectedPermissionIds)
                .build();
    }

    @Override
    @Transactional
    public void assignMenus(RoleMenuAssignCommand command) {
        roleService.getById(command.roleId());
        RoleAuthorization authorization = authorizationDomainService.replaceAuthorization(
                command.roleId(),
                command.menuIds(),
                List.of());
        replaceMenus(authorization);
    }

    @Override
    @Transactional
    public void assignPermissions(RolePermissionAssignCommand command) {
        roleService.getById(command.roleId());
        RoleAuthorization authorization = authorizationDomainService.replaceAuthorization(
                command.roleId(),
                List.of(),
                command.permissionIds());
        replacePermissions(authorization);
    }

    @Override
    @Transactional
    public void saveAuthorization(RoleAuthorizationSaveCommand command) {
        roleService.getById(command.roleId());
        RoleAuthorization authorization = authorizationDomainService.replaceAuthorization(
                command.roleId(),
                command.menuIds(),
                command.permissionIds());
        replaceMenus(authorization);
        replacePermissions(authorization);
    }

    private void replaceMenus(RoleAuthorization authorization) {
        roleMenuMapper.delete(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, authorization.roleId()));
        authorization.toRoleMenus().forEach(roleMenuMapper::insert);
    }

    private void replacePermissions(RoleAuthorization authorization) {
        rolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId, authorization.roleId()));
        authorization.toRolePermissions().forEach(rolePermissionMapper::insert);
    }
}
