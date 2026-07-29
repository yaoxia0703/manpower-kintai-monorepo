package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.system.application.dto.sys.response.RoleSummary;
import com.manpowergroup.kintai.system.application.service.sys.RoleAccessService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEmployeeRoleRepository;
import com.manpowergroup.kintai.system.domain.repository.sys.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleAccessServiceImpl implements RoleAccessService {

    private final SysEmployeeRoleRepository sysEmployeeRoleRepository;
    private final SysRoleRepository sysRoleRepository;

    @Override
    public List<RoleSummary> listEnabledByCompany(Long companyId) {
        return sysRoleRepository.listEnabledByCompanyId(companyId)
            .stream()
            .map(role -> new RoleSummary(role.getId(), role.getCode(), role.getName()))
            .toList();
    }

    @Override
    public boolean areAllEnabledForCompany(Long companyId, Collection<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return false;
        }
        long distinctRoleIds = roleIds.stream().filter(Objects::nonNull).distinct().count();
        if (distinctRoleIds != roleIds.size()) {
            return false;
        }
        Long validRoleCount = sysRoleRepository.selectCountBYCompanyIdAndRoleIds(companyId, roleIds);
        return validRoleCount == distinctRoleIds;
    }

    @Override
    public boolean employeeHasActiveRole(Long employeeId, String roleCode, LocalDate effectiveDate) {
        List<Long> roleIds = sysEmployeeRoleRepository.listByEmployee(employeeId)
            .stream()
            .filter(assignment -> assignment.isEffectiveOn(effectiveDate))
            .map(SysEmployeeRole::getRoleId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
        if (roleIds.isEmpty()) {
            return false;
        }
        return sysRoleRepository.existsEnabledByIdsAndCode(roleIds, roleCode);
    }
}
