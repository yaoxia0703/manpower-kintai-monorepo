package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SysPermissionServiceImplTest {

    @BeforeAll
    static void initializeTableInfo() {
        MapperBuilderAssistant assistant = new MapperBuilderAssistant(new MybatisConfiguration(), "");
        TableInfoHelper.initTableInfo(assistant, SysMenu.class);
        TableInfoHelper.initTableInfo(assistant, SysPermission.class);
    }

    private static SysMenu menu(long id, Long parentId) {
        return SysMenu.create(parentId, "menu-" + id, "menu-" + id,
                        "/menu-" + id, null, null, 2, (int) id, 1)
                .setId(id);
    }

    @Test
    void removeBlocksPermissionAssignedToRole() {
        SysRolePermissionMapper rolePermissionMapper = Mockito.mock(SysRolePermissionMapper.class);
        SysPermissionMapper permissionMapper = Mockito.mock(SysPermissionMapper.class);
        SysPermissionServiceImpl service = new SysPermissionServiceImpl(
                Mockito.mock(SysEmployeeRoleMapper.class),
                rolePermissionMapper,
                Mockito.mock(SysMenuMapper.class));
        ReflectionTestUtils.setField(service, "baseMapper", permissionMapper);

        when(permissionMapper.selectById(9L)).thenReturn(new SysPermission().setId(9L));
        when(rolePermissionMapper.selectCount(any())).thenReturn(1L);

        assertThrows(BizException.class, () -> service.remove(9L));

        verify(rolePermissionMapper).selectCount(any());
        verify(permissionMapper, never()).deleteById(any(Long.class));
    }

    @Test
    void pageCombinesDescendantMenusAndKeywordInDatabaseQuery() {
        SysRolePermissionMapper rolePermissionMapper = Mockito.mock(SysRolePermissionMapper.class);
        SysPermissionMapper permissionMapper = Mockito.mock(SysPermissionMapper.class);
        SysMenuMapper menuMapper = Mockito.mock(SysMenuMapper.class);
        SysPermissionServiceImpl service = new SysPermissionServiceImpl(
                Mockito.mock(SysEmployeeRoleMapper.class), rolePermissionMapper, menuMapper);
        ReflectionTestUtils.setField(service, "baseMapper", permissionMapper);

        when(menuMapper.selectList(any())).thenReturn(List.of(
                menu(1L, null), menu(2L, 1L), menu(3L, 2L), menu(9L, null)));
        when(permissionMapper.selectPage(any(Page.class), any())).thenAnswer(invocation -> {
            Page<SysPermission> page = invocation.getArgument(0);
            page.setRecords(List.of(new SysPermission().setId(20L)));
            page.setTotal(11L);
            return page;
        });

        PageResult<SysPermission> result = service.page(1L, " ADMIN ", PageRequest.of(2, 10));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Wrapper<SysPermission>> wrapperCaptor = ArgumentCaptor.forClass(Wrapper.class);
        verify(permissionMapper).selectPage(any(Page.class), wrapperCaptor.capture());
        Wrapper<SysPermission> capturedWrapper = wrapperCaptor.getValue();
        String sql = capturedWrapper.getSqlSegment();
        assertTrue(sql.contains("menu_id IN"));
        assertTrue(sql.contains("LOWER(code) LIKE"));
        assertTrue(sql.contains("LOWER(name) LIKE"));
        assertTrue(sql.contains("sort ASC"));
        assertTrue(sql.contains("id ASC"));
        AbstractWrapper<?, ?, ?> abstractWrapper = (AbstractWrapper<?, ?, ?>) capturedWrapper;
        Set<Long> longParams = abstractWrapper.getParamNameValuePairs().values().stream()
                .filter(Long.class::isInstance)
                .map(Long.class::cast)
                .collect(Collectors.toSet());
        assertEquals(Set.of(1L, 2L, 3L), longParams);
        assertEquals(11L, result.getTotal());
        assertEquals(2L, result.getPage());
        assertEquals(10L, result.getSize());
    }

    @Test
    void pageReturnsEmptyResultForUnknownMenuWithoutQueryingPermissions() {
        SysPermissionMapper permissionMapper = Mockito.mock(SysPermissionMapper.class);
        SysMenuMapper menuMapper = Mockito.mock(SysMenuMapper.class);
        SysPermissionServiceImpl service = new SysPermissionServiceImpl(
                Mockito.mock(SysEmployeeRoleMapper.class),
                Mockito.mock(SysRolePermissionMapper.class), menuMapper);
        ReflectionTestUtils.setField(service, "baseMapper", permissionMapper);
        when(menuMapper.selectList(any())).thenReturn(List.of(menu(1L, null)));

        PageResult<SysPermission> result = service.page(999L, null, PageRequest.of(1, 10));

        assertTrue(result.getRecords().isEmpty());
        assertEquals(0L, result.getTotal());
        assertEquals(1L, result.getPage());
        assertEquals(10L, result.getSize());
        verify(permissionMapper, never()).selectPage(any(Page.class), any());
    }
}
