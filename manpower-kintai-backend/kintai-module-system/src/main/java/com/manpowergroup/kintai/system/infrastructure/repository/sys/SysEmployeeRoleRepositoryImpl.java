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
}
