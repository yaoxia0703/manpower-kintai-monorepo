package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

import java.util.Collection;
import java.util.List;

public interface SysRoleRepository {
    List<SysRole> listEnabledByCompanyId(Long companyId);

    Long selectCountBYCompanyIdAndRoleIds(Long companyId, Collection<Long> roleIds);

    boolean existsEnabledByIdsAndCode(Collection<Long> roleIds, String roleCode);

    SysRole findById(Long id);

    PageResult<SysRole> pageByCompany(Long companyId, int  page, int size);

    List<SysRole> listByCompany(Long companyId);

    boolean existsByCompanyAndCode(Long companyId, String roleCode);

    void save (SysRole sysRole);

    void updateById (SysRole sysRole);

    void deleteById (Long id);

    boolean existsByCompanyAndCodeExcludingId(Long companyId, Long id,String code);

    List<SysRole> listEnadbledByIds(List<Long> ids);
}


