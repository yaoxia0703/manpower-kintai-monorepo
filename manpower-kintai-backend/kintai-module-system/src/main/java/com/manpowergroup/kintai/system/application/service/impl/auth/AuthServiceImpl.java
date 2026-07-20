package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.manpowergroup.kintai.common.dto.auth.AccessContext;
import com.manpowergroup.kintai.common.dto.auth.CurrentUserResponse;
import com.manpowergroup.kintai.common.dto.auth.LoginRequest;
import com.manpowergroup.kintai.common.dto.auth.LoginResponse;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.framework.security.jwt.JwtTokenProvider;
import com.manpowergroup.kintai.system.application.port.auth.EmployeeIdentityProvider;
import com.manpowergroup.kintai.system.application.service.auth.AccessContextService;
import com.manpowergroup.kintai.system.application.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmployeeIdentityProvider employeeIdentityProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessContextService accessContextService;

    @Override
    public LoginResponse login(LoginRequest request) {
        EmployeeIdentityProvider.LoginIdentity identity = employeeIdentityProvider
                .findLoginIdentityByEmail(request.getEmail())
                .orElseThrow(() -> new BizException(AuthErrorCode.INVALID_CREDENTIALS));

        if (identity.employeeStatus() != Status.ENABLED || identity.accountStatus() != Status.ENABLED) {
            throw new BizException(AuthErrorCode.ACCOUNT_DISABLED);
        }

        if (!passwordEncoder.matches(request.getPassword(), identity.passwordHash())) {
            throw new BizException(AuthErrorCode.INVALID_CREDENTIALS);
        }
        employeeIdentityProvider.recordSuccessfulLogin(identity.accountId(), LocalDateTime.now());

        String token = jwtTokenProvider.generateToken(identity.employeeId(), identity.accountId(), List.of());

        return LoginResponse.builder()
                .token(token)
                .employeeId(identity.employeeId())
                .accountId(identity.accountId())
                .displayName(identity.displayName())
                .email(identity.email())
                .build();
    }

    @Override
    public CurrentUserResponse me(Long employeeId, Long accountId) {
        AccessContext accessContext = accessContextService.load(employeeId, accountId);

        return CurrentUserResponse.builder()
                .user(accessContext.getUser())
                .roles(accessContext.getRoles())
                .permissions(accessContext.getPermissions())
                .menus(accessContext.getMenus())
                .build();
    }

    public enum AuthErrorCode implements BaseErrorCode {
        INVALID_CREDENTIALS(401, "error.auth.invalid_credentials"),
        ACCOUNT_DISABLED(403, "error.auth.account_disabled");

        private final int code;
        private final String messageKey;

        AuthErrorCode(int code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}
