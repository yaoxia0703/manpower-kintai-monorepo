package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

import java.util.List;

public interface SysEmployeeRoleRepository {

    List<SysEmployeeRole> listByEmployee(Long employeeId);

    SysEmployeeRole findById(Long id);

    boolean existsByEmployeeAndRoleAndCompany(Long employeeId, Long roleId, Long companyId);

    void save(SysEmployeeRole employeeRole);

    void updateById(SysEmployeeRole employeeRole);

    void deleteById(Long id);

    boolean existsById(Long id);

}
