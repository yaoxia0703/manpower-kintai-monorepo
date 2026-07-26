package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

import java.util.Collection;
import java.util.List;

public interface SysRoleRepository {
    List<SysRole> findByCompanyId(Long companyId);

    Long selectCountBYCompanyIdAndRoleIds(Long companyId, Collection<Long> roleIds);

    boolean existsEnabledByIdsAndCode(Collection<Long> roleIds, String roleCode);
}
