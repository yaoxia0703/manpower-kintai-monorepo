package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEmployeeRoleRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysEmployeeRoleRepositoryImpl  implements SysEmployeeRoleRepository {
    private final SysEmployeeRoleMapper sysEmployeeRoleMapper;

    public SysEmployeeRoleRepositoryImpl(SysEmployeeRoleMapper sysEmployeeRoleMapper) {
        this.sysEmployeeRoleMapper = sysEmployeeRoleMapper;
    }

    @Override
    public List<SysEmployeeRole> listByEmployee(Long employeeId) {
        return sysEmployeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
            .eq(SysEmployeeRole::getEmployeeId, employeeId));
    }

    @Override
    public SysEmployeeRole findById(Long id) {
        return sysEmployeeRoleMapper.selectById(id);
    }

    @Override
    public boolean existsByEmployeeAndRoleAndCompany(Long employeeId, Long roleId, Long companyId) {
        return sysEmployeeRoleMapper.selectCount(Wrappers.<SysEmployeeRole>lambdaQuery()
            .eq(SysEmployeeRole::getEmployeeId, employeeId)
            .eq(SysEmployeeRole::getRoleId, roleId)
            .eq(SysEmployeeRole::getCompanyId, companyId)) > 0;
    }

    @Override
    public void save(SysEmployeeRole employeeRole) {
        sysEmployeeRoleMapper.insert(employeeRole);
    }

    @Override
    public void updateById(SysEmployeeRole employeeRole) {
        sysEmployeeRoleMapper.updateById(employeeRole);
    }

    @Override
    public void deleteById(Long id) {
        sysEmployeeRoleMapper.deleteById(id);
    }
}
