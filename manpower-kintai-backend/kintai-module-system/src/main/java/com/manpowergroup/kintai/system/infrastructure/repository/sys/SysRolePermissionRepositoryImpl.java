package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRolePermissionRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRolePermissionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRolePermissionRepositoryImpl implements SysRolePermissionRepository {

    private final SysRolePermissionMapper  sysRolePermissionMapper;

    public SysRolePermissionRepositoryImpl(SysRolePermissionMapper sysRolePermissionMapper) {
        this.sysRolePermissionMapper = sysRolePermissionMapper;
    }

    @Override
    public List<SysRolePermission> listByRoleId(Long roleId) {
        return sysRolePermissionMapper.selectList(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId, roleId));
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        sysRolePermissionMapper.delete(Wrappers.<SysRolePermission>lambdaQuery().eq(SysRolePermission::getRoleId, roleId));
    }

    @Override
    public void save(SysRolePermission sysRolePermission) {
        sysRolePermissionMapper.insert(sysRolePermission);
    }

    @Override
    public List<SysRolePermission> listByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return List.of();
        }
        return sysRolePermissionMapper.selectList(new LambdaQueryWrapper<SysRolePermission>()
            .in(SysRolePermission::getRoleId, roleIds));
    }

    @Override
    public boolean existsByPermissionId(Long permissionId) {
        return sysRolePermissionMapper.selectCount(new LambdaQueryWrapper<SysRolePermission>()
            .eq(SysRolePermission::getPermissionId, permissionId)) > 0;
    }
}
