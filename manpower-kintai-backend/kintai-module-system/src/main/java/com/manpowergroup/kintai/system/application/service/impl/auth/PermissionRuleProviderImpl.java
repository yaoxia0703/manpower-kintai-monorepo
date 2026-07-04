package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.framework.security.authority.PermissionRule;
import com.manpowergroup.kintai.framework.security.authority.PermissionRuleProvider;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysPermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PermissionRuleProviderImpl implements PermissionRuleProvider {

    private final SysPermissionMapper permissionMapper;

    @Override
    public List<PermissionRule> loadEnabledRules() {
        return permissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery()
                        .eq(SysPermission::getStatus, Status.ENABLED)
                        .orderByAsc(SysPermission::getSort))
                .stream()
                .filter(Objects::nonNull)
                .map(permission -> new PermissionRule(
                        permission.getCode(),
                        permission.getMethod(),
                        permission.getPath()))
                .toList();
    }
}
