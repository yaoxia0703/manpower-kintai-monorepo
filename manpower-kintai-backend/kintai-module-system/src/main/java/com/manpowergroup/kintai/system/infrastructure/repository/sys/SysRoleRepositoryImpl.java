package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
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
    public List<SysRole> listEnabledByCompanyId(Long companyId) {
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

    @Override
    public SysRole findById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    @Override
    public PageResult<SysRole> pageByCompany(Long companyId, int page, int size) {
        Page<SysRole> p = new Page<>(page, size);
        sysRoleMapper.selectPage(p, Wrappers.<SysRole>lambdaQuery()
//            .eq(SysRole::getCompanyId, companyId)
            .orderByAsc(SysRole::getSort));
        return PageResult.of(p);
    }

    @Override
    public List<SysRole> listByCompany(Long companyId) {
        return sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
            .eq(companyId != null, SysRole::getCompanyId, companyId)
            .orderByAsc(SysRole::getSort));
    }

    @Override
    public boolean existsByCompanyAndCode(Long companyId, String roleCode) {
        return sysRoleMapper.selectCount(Wrappers.<SysRole>lambdaQuery()
            .eq(companyId != null, SysRole::getCompanyId, companyId)
            .isNull(companyId == null, SysRole::getCompanyId)
            .eq(SysRole::getCode, roleCode)
        ) > 0;
    }

    @Override
    public void save(SysRole sysRole) {
        sysRoleMapper.insert(sysRole);
    }

    @Override
    public void updateById(SysRole sysRole) {
        sysRoleMapper.updateById(sysRole);
    }

    @Override
    public void deleteById(Long id) {
        sysRoleMapper.deleteById(id);
    }

    @Override
    public boolean existsByCompanyAndCodeExcludingId(Long companyId, Long id, String code) {
        return sysRoleMapper.selectCount(Wrappers.<SysRole>lambdaQuery()
            .eq(companyId != null, SysRole::getCompanyId, companyId)
            .isNull(companyId == null, SysRole::getCompanyId)
            .eq(SysRole::getCode, code)
            .ne(SysRole::getId, id)
        ) > 0;
    }

    @Override
    public List<SysRole> listEnadbledByIds(List<Long> ids) {
        return sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
            .in(SysRole::getId, ids)
            .eq(SysRole::getStatus, Status.ENABLED)
        );
    }
}
