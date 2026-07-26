package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.manpowergroup.kintai.framework.security.authority.PermissionRule;
import com.manpowergroup.kintai.framework.security.authority.PermissionRuleProvider;
import com.manpowergroup.kintai.system.domain.repository.sys.SysPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PermissionRuleProviderImpl implements PermissionRuleProvider {

    private final SysPermissionRepository permissionRepository;

    @Override
    public List<PermissionRule> loadEnabledRules() {
        return permissionRepository.loadEnabled()
            .stream()
            .map(p -> new PermissionRule(p.getCode(), p.getMethod(), p.getPath()))
            .toList();
    }
}
