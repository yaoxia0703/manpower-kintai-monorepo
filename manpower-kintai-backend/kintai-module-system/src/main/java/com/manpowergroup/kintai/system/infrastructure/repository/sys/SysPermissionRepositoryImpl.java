package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRolePermission;
import com.manpowergroup.kintai.system.domain.repository.sys.SysPermissionRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysPermissionRepositoryImpl implements SysPermissionRepository {
    private final SysPermissionMapper sysPermissionMapper;

    public SysPermissionRepositoryImpl(SysPermissionMapper sysPermissionMapper) {
        this.sysPermissionMapper = sysPermissionMapper;
    }

    @Override
    public List<SysPermission> loadEnabled() {
        return sysPermissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
            .eq(SysPermission::getStatus, Status.ENABLED)
            .orderByAsc(SysPermission::getSort));
    }

    @Override
    public List<SysPermission> findAll() {
        return sysPermissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery().orderByAsc(SysPermission::getSort));
    }


}
