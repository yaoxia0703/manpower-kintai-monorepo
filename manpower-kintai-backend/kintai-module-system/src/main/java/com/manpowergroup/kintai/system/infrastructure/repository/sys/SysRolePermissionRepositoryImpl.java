package com.manpowergroup.kintai.system.infrastructure.repository.sys;

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
    public List<SysRolePermission> findByRoleId(Long roleId) {
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
}
