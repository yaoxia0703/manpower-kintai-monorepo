package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.application.dto.sys.response.RoleSummary;
import com.manpowergroup.kintai.system.application.service.sys.RoleAccessService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleAccessServiceImpl implements RoleAccessService {

    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public List<RoleSummary> listEnabledByCompany(Long companyId) {
        return roleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                        .eq(SysRole::getCompanyId, companyId)
                        .eq(SysRole::getStatus, Status.ENABLED)
                        .orderByAsc(SysRole::getSort))
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
        Long validRoleCount = roleMapper.selectCount(Wrappers.<SysRole>lambdaQuery()
                .in(SysRole::getId, roleIds)
                .eq(SysRole::getCompanyId, companyId)
                .eq(SysRole::getStatus, Status.ENABLED));
        return validRoleCount == distinctRoleIds;
    }

    @Override
    public boolean employeeHasActiveRole(Long employeeId, String roleCode, LocalDate effectiveDate) {
        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId))
                .stream()
                .filter(assignment -> assignment.isEffectiveOn(effectiveDate))
                .map(SysEmployeeRole::getRoleId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (roleIds.isEmpty()) {
            return false;
        }
        return roleMapper.selectCount(Wrappers.<SysRole>lambdaQuery()
                .in(SysRole::getId, roleIds)
                .eq(SysRole::getCode, roleCode)
                .eq(SysRole::getStatus, Status.ENABLED)) > 0;
    }
}
