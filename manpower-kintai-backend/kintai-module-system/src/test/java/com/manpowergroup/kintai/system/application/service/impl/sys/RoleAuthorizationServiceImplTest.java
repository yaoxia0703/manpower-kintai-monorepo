package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleAuthorizationSaveCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleMenuAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.RolePermissionAssignCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.domain.repository.sys.SysMenuRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysPermissionRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleMenuRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRolePermissionRepository;
import com.manpowergroup.kintai.system.domain.service.sys.RoleAuthorizationDomainService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RoleAuthorizationServiceImplTest {

    @Test
    void saveAuthorizationReplacesMenusAndPermissionsTogetherWithNormalizedIds() {
        SysRoleMenuRepository roleMenuRepository = Mockito.mock(SysRoleMenuRepository.class);
        SysRolePermissionRepository rolePermissionRepository = Mockito.mock(SysRolePermissionRepository.class);
        SysRoleService roleService = Mockito.mock(SysRoleService.class);
        RoleAuthorizationServiceImpl service = new RoleAuthorizationServiceImpl(
                roleService,
                new RoleAuthorizationDomainService(),
                roleMenuRepository,
                rolePermissionRepository,
                Mockito.mock(SysMenuRepository.class),
                Mockito.mock(SysPermissionRepository.class));

        when(roleService.getById(7L)).thenReturn(new SysRole().setId(7L));
        RoleAuthorizationSaveCommand command = new RoleAuthorizationSaveCommand(
                7L,
                Arrays.asList(1L, null, 2L, 1L),
                Arrays.asList(10L, null, 11L, 10L));

        service.saveAuthorization(command);

        verify(roleMenuRepository).deleteByRoleId(7L);
        verify(rolePermissionRepository).deleteByRoleId(7L);

        ArgumentCaptor<SysRoleMenu> menuCaptor = ArgumentCaptor.forClass(SysRoleMenu.class);
        ArgumentCaptor<SysRolePermission> permissionCaptor = ArgumentCaptor.forClass(SysRolePermission.class);
        verify(roleMenuRepository, times(2)).save(menuCaptor.capture());
        verify(rolePermissionRepository, times(2)).save(permissionCaptor.capture());

        assertEquals(List.of(1L, 2L), menuCaptor.getAllValues().stream().map(SysRoleMenu::getMenuId).toList());
        assertEquals(List.of(7L, 7L), menuCaptor.getAllValues().stream().map(SysRoleMenu::getRoleId).toList());
        assertEquals(List.of(10L, 11L), permissionCaptor.getAllValues().stream().map(SysRolePermission::getPermissionId).toList());
        assertEquals(List.of(7L, 7L), permissionCaptor.getAllValues().stream().map(SysRolePermission::getRoleId).toList());
    }

    @Test
    void assignMenusReplacesOnlyMenuLinks() {
        SysRoleMenuRepository roleMenuRepository = Mockito.mock(SysRoleMenuRepository.class);
        SysRolePermissionRepository rolePermissionRepository = Mockito.mock(SysRolePermissionRepository.class);
        SysRoleService roleService = Mockito.mock(SysRoleService.class);
        RoleAuthorizationServiceImpl service = new RoleAuthorizationServiceImpl(
                roleService,
                new RoleAuthorizationDomainService(),
                roleMenuRepository,
                rolePermissionRepository,
                Mockito.mock(SysMenuRepository.class),
                Mockito.mock(SysPermissionRepository.class));

        when(roleService.getById(7L)).thenReturn(new SysRole().setId(7L));

        service.assignMenus(new RoleMenuAssignCommand(7L, Arrays.asList(1L, null, 2L, 1L)));

        verify(roleMenuRepository).deleteByRoleId(7L);
        ArgumentCaptor<SysRoleMenu> menuCaptor = ArgumentCaptor.forClass(SysRoleMenu.class);
        verify(roleMenuRepository, times(2)).save(menuCaptor.capture());
        verify(rolePermissionRepository, never()).deleteByRoleId(7L);
        verify(rolePermissionRepository, never()).save(Mockito.any(SysRolePermission.class));
        assertEquals(List.of(1L, 2L), menuCaptor.getAllValues().stream().map(SysRoleMenu::getMenuId).toList());
    }

    @Test
    void assignPermissionsReplacesOnlyPermissionLinks() {
        SysRoleMenuRepository roleMenuRepository = Mockito.mock(SysRoleMenuRepository.class);
        SysRolePermissionRepository rolePermissionRepository = Mockito.mock(SysRolePermissionRepository.class);
        SysRoleService roleService = Mockito.mock(SysRoleService.class);
        RoleAuthorizationServiceImpl service = new RoleAuthorizationServiceImpl(
                roleService,
                new RoleAuthorizationDomainService(),
                roleMenuRepository,
                rolePermissionRepository,
                Mockito.mock(SysMenuRepository.class),
                Mockito.mock(SysPermissionRepository.class));

        when(roleService.getById(7L)).thenReturn(new SysRole().setId(7L));

        service.assignPermissions(new RolePermissionAssignCommand(7L, Arrays.asList(10L, null, 11L, 10L)));

        verify(rolePermissionRepository).deleteByRoleId(7L);
        ArgumentCaptor<SysRolePermission> permissionCaptor = ArgumentCaptor.forClass(SysRolePermission.class);
        verify(rolePermissionRepository, times(2)).save(permissionCaptor.capture());
        verify(roleMenuRepository, never()).deleteByRoleId(7L);
        verify(roleMenuRepository, never()).save(Mockito.any(SysRoleMenu.class));
        assertEquals(List.of(10L, 11L), permissionCaptor.getAllValues().stream().map(SysRolePermission::getPermissionId).toList());
    }
}
