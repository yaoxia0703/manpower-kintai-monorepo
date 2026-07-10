package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleAuthorizationSaveCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SysRoleServiceImplTest {

    @Test
    void saveAuthorizationReplacesMenusAndPermissionsTogether() {
        SysRoleMenuMapper roleMenuMapper = Mockito.mock(SysRoleMenuMapper.class);
        SysRolePermissionMapper rolePermissionMapper = Mockito.mock(SysRolePermissionMapper.class);
        SysRoleService roleService = Mockito.mock(SysRoleService.class);
        RoleAuthorizationServiceImpl service = new RoleAuthorizationServiceImpl(
                roleService,
                roleMenuMapper,
                rolePermissionMapper,
                Mockito.mock(SysMenuMapper.class),
                Mockito.mock(SysPermissionMapper.class));

        when(roleService.getById(7L)).thenReturn(new SysRole().setId(7L));
        RoleAuthorizationSaveCommand command = new RoleAuthorizationSaveCommand(7L, List.of(1L, 2L), List.of(10L, 11L));

        service.saveAuthorization(command);

        verify(roleMenuMapper).delete(any());
        verify(rolePermissionMapper).delete(any());

        ArgumentCaptor<com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu> menuCaptor =
                ArgumentCaptor.forClass(com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu.class);
        ArgumentCaptor<com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission> permissionCaptor =
                ArgumentCaptor.forClass(com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission.class);
        verify(roleMenuMapper, times(2)).insert(menuCaptor.capture());
        verify(rolePermissionMapper, times(2)).insert(permissionCaptor.capture());

        assertEquals(List.of(1L, 2L), menuCaptor.getAllValues().stream().map(com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu::getMenuId).toList());
        assertEquals(List.of(10L, 11L), permissionCaptor.getAllValues().stream().map(com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission::getPermissionId).toList());
    }
}
