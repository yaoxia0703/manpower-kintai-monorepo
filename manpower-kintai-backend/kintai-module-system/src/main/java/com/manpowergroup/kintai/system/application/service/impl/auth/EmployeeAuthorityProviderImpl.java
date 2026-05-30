package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.manpowergroup.kintai.framework.security.authority.EmployeeAuthorityProvider;
import com.manpowergroup.kintai.system.application.service.auth.AccessContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeAuthorityProviderImpl implements EmployeeAuthorityProvider {

    private final AccessContextService accessContextService;

    @Override
    public List<String> loadPermissionCodes(Long employeeId) {
        return accessContextService.load(employeeId, null).getPermissions();
    }

    @Override
    public List<String> loadAuthorityCodes(Long employeeId, Long accountId) {
        return accessContextService.load(employeeId, accountId).getAuthorities();
    }
}
