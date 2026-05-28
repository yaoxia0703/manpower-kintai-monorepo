package com.manpowergroup.kintai.system.application.service.impl.auth;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.dto.auth.CurrentUserResponse;
import com.manpowergroup.kintai.common.dto.auth.LoginRequest;
import com.manpowergroup.kintai.common.dto.auth.LoginResponse;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.framework.security.authority.EmployeeAuthorityProvider;
import com.manpowergroup.kintai.framework.security.jwt.JwtTokenProvider;
import com.manpowergroup.kintai.system.application.service.auth.AuthService;
import com.manpowergroup.kintai.system.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.domain.repository.emp.EmpEmployeeRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// 認証サービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmpEmployeeRepository employeeRepository;
    private final EmpAccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SysMenuService menuService;
    private final EmployeeAuthorityProvider authorityProvider;
    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        // メールアドレスで社員を検索
        EmpEmployee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BizException(AuthErrorCode.INVALID_CREDENTIALS));

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
        EmpEmployee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BizException(AuthErrorCode.INVALID_CREDENTIALS));

        LocalDate today = LocalDate.now();
        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId)
                        .and(q -> q.isNull(SysEmployeeRole::getStartDate)
                                .or()
                                .le(SysEmployeeRole::getStartDate, today))
                        .and(q -> q.isNull(SysEmployeeRole::getEndDate)
                                .or()
                                .ge(SysEmployeeRole::getEndDate, today)))
                .stream()
                .map(SysEmployeeRole::getRoleId)
                .distinct()
                .toList();

        List<String> roles = roleIds.isEmpty()
                ? List.of()
                : roleMapper.selectList(Wrappers.<SysRole>lambdaQuery()
                                .in(SysRole::getId, roleIds)
                                .eq(SysRole::getStatus, Status.ENABLED))
                        .stream()
                        .map(SysRole::getCode)
                        .distinct()
                        .toList();

        List<String> permissions = authorityProvider.loadPermissionCodes(employeeId);
        List<CurrentUserResponse.MenuItem> menus = menuService.listByEmployeeId(employeeId)
                .stream()
                .filter(menu -> menu.getStatus() == Status.ENABLED)
                .filter(menu -> menu.getVisible() == null || menu.getVisible() == 1)
                .map(this::toMenuItem)
                .toList();

        return CurrentUserResponse.builder()
                .user(CurrentUserResponse.UserProfile.builder()
                        .employeeId(employee.getId())
                        .accountId(accountId)
                        .companyId(employee.getCompanyId())
                        .employeeCode(employee.getEmployeeCode())
                        .displayName(employee.getLastName() + " " + employee.getFirstName())
                        .email(employee.getEmail())
                        .build())
                .roles(roles)
                .permissions(permissions)
                .menus(menus)
                .build();
    }

    private CurrentUserResponse.MenuItem toMenuItem(SysMenu menu) {
        return CurrentUserResponse.MenuItem.builder()
                .id(menu.getId())
                .parentId(menu.getParentId())
                .name(menu.getName())
                .code(menu.getCode())
                .path(menu.getPath())
                .component(menu.getComponent())
                .icon(menu.getIcon())
                .type(menu.getType())
                .sort(menu.getSort())
                .visible(menu.getVisible())
                .build();
    }

    enum AuthErrorCode implements BaseErrorCode {
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
