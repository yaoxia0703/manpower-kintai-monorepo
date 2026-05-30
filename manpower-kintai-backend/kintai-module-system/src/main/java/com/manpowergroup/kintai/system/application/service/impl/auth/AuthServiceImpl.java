package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.manpowergroup.kintai.common.dto.auth.AccessContext;
import com.manpowergroup.kintai.common.dto.auth.CurrentUserResponse;
import com.manpowergroup.kintai.common.dto.auth.LoginRequest;
import com.manpowergroup.kintai.common.dto.auth.LoginResponse;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.framework.security.jwt.JwtTokenProvider;
import com.manpowergroup.kintai.system.application.service.auth.AccessContextService;
import com.manpowergroup.kintai.system.application.service.auth.AuthService;
import com.manpowergroup.kintai.system.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.repository.emp.EmpEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

// 認証サービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmpEmployeeRepository employeeRepository;
    private final EmpAccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessContextService accessContextService;

    @Override
    public LoginResponse login(LoginRequest request) {
        // メールアドレスで社員を検索
        EmpEmployee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BizException(AuthErrorCode.INVALID_CREDENTIALS));

        if (employee.getStatus() != Status.ENABLED) {
            throw new BizException(AuthErrorCode.ACCOUNT_DISABLED);
        }

        // 社員IDでアカウントを取得
        EmpAccount account = accountService.getByEmployeeId(employee.getId());

        // アカウント有効チェック
        if (account.getStatus() != Status.ENABLED) {
            throw new BizException(AuthErrorCode.ACCOUNT_DISABLED);
        }

        // パスワード検証
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new BizException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        // JWTトークン生成
        String token = jwtTokenProvider.generateToken(employee.getId(), account.getId(), List.of());

        return LoginResponse.builder()
                .token(token)
                .employeeId(employee.getId())
                .accountId(account.getId())
                .displayName(employee.getLastName() + " " + employee.getFirstName())
                .email(employee.getEmail())
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
