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
import com.manpowergroup.kintai.system.domain.repository.sys.SysMenuRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysPermissionRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleMenuRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRolePermissionRepository;
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
    private final RoleAuthorizationDomainService authorizationDomainService;
    private final SysRoleMenuRepository sysRoleMenuRepository;
    private final SysRolePermissionRepository sysRolePermissionRepository;
    private final SysMenuRepository sysMenuRepository;
    private final SysPermissionRepository sysPermissionRepository;
//    private final SysRoleMenuMapper roleMenuMapper;
//    private final SysRolePermissionMapper rolePermissionMapper;
//    private final SysMenuMapper menuMapper;
//    private final SysPermissionMapper permissionMapper;


    @Override
    public RoleAuthorizationResponse getAuthorization(Long roleId) {
        roleService.getById(roleId);
        List<Long> selectedMenuIds = sysRoleMenuRepository.findByRoleId(roleId)
            .stream()
            .map(SysRoleMenu::getMenuId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
        List<Long> selectedPermissionIds = sysRolePermissionRepository.findByRoleId(roleId)
            .stream()
            .map(SysRolePermission::getPermissionId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
        List<MenuResponse> menus = sysMenuRepository.findAll()
            .stream()
            .map(MenuResponse::from)
            .toList();
        List<SysPermission> permissions = sysPermissionRepository.findAll();

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
        sysRoleMenuRepository.deleteByRoleId(authorization.roleId());
        authorization.toRoleMenus().forEach(sysRoleMenuRepository::save);
    }

    private void replacePermissions(RoleAuthorization authorization) {
        sysRolePermissionRepository.deleteByRoleId(authorization.roleId());
        authorization.toRolePermissions().forEach(sysRolePermissionRepository::save);
    }
}
