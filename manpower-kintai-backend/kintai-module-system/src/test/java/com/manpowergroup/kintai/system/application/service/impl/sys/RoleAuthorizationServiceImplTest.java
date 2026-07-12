package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleAuthorizationSaveCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleMenuAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.RolePermissionAssignCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.domain.service.sys.RoleAuthorizationDomainService;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoleAuthorizationServiceImplTest {

    @Test
    void saveAuthorizationReplacesMenusAndPermissionsTogetherWithNormalizedIds() {
        SysRoleMenuMapper roleMenuMapper = Mockito.mock(SysRoleMenuMapper.class);
        SysRolePermissionMapper rolePermissionMapper = Mockito.mock(SysRolePermissionMapper.class);
        SysRoleService roleService = Mockito.mock(SysRoleService.class);
        RoleAuthorizationServiceImpl service = new RoleAuthorizationServiceImpl(
                roleService,
                roleMenuMapper,
                rolePermissionMapper,
                Mockito.mock(SysMenuMapper.class),
                Mockito.mock(SysPermissionMapper.class),
                new RoleAuthorizationDomainService());

        when(roleService.getById(7L)).thenReturn(new SysRole().setId(7L));
        RoleAuthorizationSaveCommand command = new RoleAuthorizationSaveCommand(
                7L,
                Arrays.asList(1L, null, 2L, 1L),
                Arrays.asList(10L, null, 11L, 10L));

        service.saveAuthorization(command);

        verify(roleMenuMapper).delete(any());
        verify(rolePermissionMapper).delete(any());

        ArgumentCaptor<SysRoleMenu> menuCaptor = ArgumentCaptor.forClass(SysRoleMenu.class);
        ArgumentCaptor<SysRolePermission> permissionCaptor = ArgumentCaptor.forClass(SysRolePermission.class);
        verify(roleMenuMapper, times(2)).insert(menuCaptor.capture());
        verify(rolePermissionMapper, times(2)).insert(permissionCaptor.capture());

        assertEquals(List.of(1L, 2L), menuCaptor.getAllValues().stream().map(SysRoleMenu::getMenuId).toList());
        assertEquals(List.of(7L, 7L), menuCaptor.getAllValues().stream().map(SysRoleMenu::getRoleId).toList());
        assertEquals(List.of(10L, 11L), permissionCaptor.getAllValues().stream().map(SysRolePermission::getPermissionId).toList());
        assertEquals(List.of(7L, 7L), permissionCaptor.getAllValues().stream().map(SysRolePermission::getRoleId).toList());
    }

    @Test
    void assignMenusReplacesOnlyMenuLinks() {
        SysRoleMenuMapper roleMenuMapper = Mockito.mock(SysRoleMenuMapper.class);
        SysRolePermissionMapper rolePermissionMapper = Mockito.mock(SysRolePermissionMapper.class);
        SysRoleService roleService = Mockito.mock(SysRoleService.class);
        RoleAuthorizationServiceImpl service = new RoleAuthorizationServiceImpl(
                roleService,
                roleMenuMapper,
                rolePermissionMapper,
                Mockito.mock(SysMenuMapper.class),
                Mockito.mock(SysPermissionMapper.class),
                new RoleAuthorizationDomainService());

        when(roleService.getById(7L)).thenReturn(new SysRole().setId(7L));

        service.assignMenus(new RoleMenuAssignCommand(7L, Arrays.asList(1L, null, 2L, 1L)));

        verify(roleMenuMapper).delete(any());
        ArgumentCaptor<SysRoleMenu> menuCaptor = ArgumentCaptor.forClass(SysRoleMenu.class);
        verify(roleMenuMapper, times(2)).insert(menuCaptor.capture());
        verify(rolePermissionMapper, never()).delete(any());
        verify(rolePermissionMapper, never()).insert(any(SysRolePermission.class));
        assertEquals(List.of(1L, 2L), menuCaptor.getAllValues().stream().map(SysRoleMenu::getMenuId).toList());
    }

    @Test
    void assignPermissionsReplacesOnlyPermissionLinks() {
        SysRoleMenuMapper roleMenuMapper = Mockito.mock(SysRoleMenuMapper.class);
        SysRolePermissionMapper rolePermissionMapper = Mockito.mock(SysRolePermissionMapper.class);
        SysRoleService roleService = Mockito.mock(SysRoleService.class);
        RoleAuthorizationServiceImpl service = new RoleAuthorizationServiceImpl(
                roleService,
                roleMenuMapper,
                rolePermissionMapper,
                Mockito.mock(SysMenuMapper.class),
                Mockito.mock(SysPermissionMapper.class),
                new RoleAuthorizationDomainService());

        when(roleService.getById(7L)).thenReturn(new SysRole().setId(7L));

        service.assignPermissions(new RolePermissionAssignCommand(7L, Arrays.asList(10L, null, 11L, 10L)));

        verify(rolePermissionMapper).delete(any());
        ArgumentCaptor<SysRolePermission> permissionCaptor = ArgumentCaptor.forClass(SysRolePermission.class);
        verify(rolePermissionMapper, times(2)).insert(permissionCaptor.capture());
        verify(roleMenuMapper, never()).delete(any());
        verify(roleMenuMapper, never()).insert(any(SysRoleMenu.class));
        assertEquals(List.of(10L, 11L), permissionCaptor.getAllValues().stream().map(SysRolePermission::getPermissionId).toList());
    }
}
