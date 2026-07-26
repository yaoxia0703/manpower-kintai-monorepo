package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class SysRoleRepositoryImpl implements SysRoleRepository {

    private final SysRoleMapper sysRoleMapper;

    public SysRoleRepositoryImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public List<SysRole> findByCompanyId(Long companyId) {
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>()
            .eq(SysRole::getCompanyId, companyId)
            .eq(SysRole::getStatus, Status.ENABLED)
            .orderByAsc(SysRole::getSort)
        );
    }

    @Override
    public Long selectCountBYCompanyIdAndRoleIds(Long companyId, Collection<Long> roleIds) {
        return sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>()
            .in(SysRole::getId, roleIds)
            .eq(SysRole::getCompanyId, companyId)
            .eq(SysRole::getStatus, Status.ENABLED));
    }

    @Override
    public boolean existsEnabledByIdsAndCode(Collection<Long> roleIds, String roleCode) {
        if (roleIds == null || roleIds.isEmpty()) {
            return false;
        }
        return sysRoleMapper.selectCount(Wrappers.<SysRole>lambdaQuery()
            .in(SysRole::getId, roleIds)
            .eq(SysRole::getCode, roleCode)
            .eq(SysRole::getStatus, Status.ENABLED)) > 0;
    }
}
