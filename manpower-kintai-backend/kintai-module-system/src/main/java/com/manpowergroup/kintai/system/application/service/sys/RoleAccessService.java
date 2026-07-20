package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.system.application.dto.sys.response.RoleSummary;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Public application boundary for business modules that need RBAC role data.
 */
public interface RoleAccessService {

    List<RoleSummary> listEnabledByCompany(Long companyId);

    boolean areAllEnabledForCompany(Long companyId, Collection<Long> roleIds);

    boolean employeeHasActiveRole(Long employeeId, String roleCode, LocalDate effectiveDate);
}
