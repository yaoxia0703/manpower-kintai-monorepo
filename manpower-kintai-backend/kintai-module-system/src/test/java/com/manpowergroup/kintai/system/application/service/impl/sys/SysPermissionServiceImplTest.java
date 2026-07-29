package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.PermissionHttpMethod;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.PermissionCreateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEmployeeRoleRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysMenuRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysPermissionRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRolePermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SysPermissionServiceImplTest {

    private SysPermissionRepository permissionRepository;
    private SysRolePermissionRepository rolePermissionRepository;
    private SysMenuRepository menuRepository;
    private SysPermissionServiceImpl service;

    @BeforeEach
    void setUp() {
        permissionRepository = mock(SysPermissionRepository.class);
        rolePermissionRepository = mock(SysRolePermissionRepository.class);
        menuRepository = mock(SysMenuRepository.class);
        service = new SysPermissionServiceImpl(
            permissionRepository,
            rolePermissionRepository,
            mock(SysEmployeeRoleRepository.class),
            menuRepository);
    }

    @Test
    void removeBlocksPermissionAssignedToRole() {
        when(permissionRepository.findById(9L)).thenReturn(new SysPermission().setId(9L));
        when(rolePermissionRepository.existsByPermissionId(9L)).thenReturn(true);

        assertThrows(BizException.class, () -> service.remove(9L));

        verify(permissionRepository, never()).deleteById(any(Long.class));
    }

    @Test
    void pageDelegatesDescendantMenuIdsAndKeywordToRepository() {
        PageResult<SysPermission> expected = PageResult.empty(2, 10);
        when(menuRepository.listSelfAndDescendantIds(1L)).thenReturn(List.of(1L, 2L, 3L));
        when(permissionRepository.findPageByMenuIdsAndKeyword(
            List.of(1L, 2L, 3L), " ADMIN ", 2, 10)).thenReturn(expected);

        PageResult<SysPermission> result = service.page(1L, " ADMIN ", PageRequest.of(2, 10));

        assertEquals(expected, result);
        verify(permissionRepository).findPageByMenuIdsAndKeyword(
            List.of(1L, 2L, 3L), " ADMIN ", 2, 10);
    }

    @Test
    void pageWithoutMenuDelegatesWithEmptyMenuIds() {
        PageResult<SysPermission> expected = PageResult.empty(1, 10);
        when(permissionRepository.findPageByMenuIdsAndKeyword(List.of(), " Admin%_! ", 1, 10))
            .thenReturn(expected);

        PageResult<SysPermission> result = service.page(null, " Admin%_! ", PageRequest.of(1, 10));

        assertEquals(expected, result);
        verify(menuRepository, never()).listSelfAndDescendantIds(any(Long.class));
    }

    @Test
    void pageReturnsEmptyResultForUnknownMenuWithoutQueryingPermissions() {
        when(menuRepository.listSelfAndDescendantIds(999L)).thenReturn(List.of());

        PageResult<SysPermission> result = service.page(999L, null, PageRequest.of(1, 10));

        assertTrue(result.getRecords().isEmpty());
        assertEquals(0L, result.getTotal());
        assertEquals(1L, result.getPage());
        assertEquals(10L, result.getSize());
        verify(permissionRepository, never())
            .findPageByMenuIdsAndKeyword(any(), any(), any(Integer.class), any(Integer.class));
    }

    @Test
    void createAcceptsExistingMenu() {
        when(menuRepository.existsById(1L)).thenReturn(true);
        when(permissionRepository.existsByCodeExcludingId("employee:read", null)).thenReturn(false);

        SysPermission created = service.create(new PermissionCreateCommand(
            1L,
            "employee:read",
            "Read employee",
            PermissionHttpMethod.GET,
            "/employees/**",
            null,
            1));

        assertEquals(1L, created.getMenuId());
        verify(permissionRepository).save(created);
    }

    @Test
    void createRejectsUnknownMenu() {
        when(menuRepository.existsById(999L)).thenReturn(false);

        assertThrows(BizException.class, () -> service.create(new PermissionCreateCommand(
            999L,
            "employee:read",
            "Read employee",
            PermissionHttpMethod.GET,
            "/employees/**",
            null,
            1)));

        verify(permissionRepository, never()).save(any());
    }
}
