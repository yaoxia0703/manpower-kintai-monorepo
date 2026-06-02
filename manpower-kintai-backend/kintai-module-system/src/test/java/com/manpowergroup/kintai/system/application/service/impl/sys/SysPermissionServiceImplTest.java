package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SysPermissionServiceImplTest {

    @Test
    void removeBlocksPermissionAssignedToRole() {
        SysRolePermissionMapper rolePermissionMapper = Mockito.mock(SysRolePermissionMapper.class);
        SysPermissionMapper permissionMapper = Mockito.mock(SysPermissionMapper.class);
        SysPermissionServiceImpl service = new SysPermissionServiceImpl(
                Mockito.mock(SysEmployeeRoleMapper.class),
                rolePermissionMapper);
        ReflectionTestUtils.setField(service, "baseMapper", permissionMapper);

        when(permissionMapper.selectById(9L)).thenReturn(new SysPermission().setId(9L));
        when(rolePermissionMapper.selectList(any())).thenReturn(List.of(new SysRolePermission().setRoleId(1L).setPermissionId(9L)));

        assertThrows(BizException.class, () -> service.remove(9L));

        verify(rolePermissionMapper).selectList(any());
        verify(permissionMapper, never()).deleteById(any(Long.class));
    }
}
