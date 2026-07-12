package com.manpowergroup.kintai.framework.security.authority;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

@Component
public class DynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    // 個人自助系エンドポイント：ログインさえしていれば sys_permission を経由せず許可
    private static final List<String> AUTHENTICATED_ONLY_PATHS = List.of(
            "/employee/notifications/**"
    );

    private final PermissionRuleProvider permissionRuleProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public DynamicAuthorizationManager(PermissionRuleProvider permissionRuleProvider) {
        this.permissionRuleProvider = permissionRuleProvider;
    }


    @Override
    @SuppressWarnings("deprecation")
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication current = authentication.get();
        if (current == null || !current.isAuthenticated() || isAnonymous(current)) {
            return new AuthorizationDecision(false);
        }
        if (hasAuthority(current, SUPER_ADMIN)) {
            return new AuthorizationDecision(true);
        }

        HttpServletRequest request = context.getRequest();
        String method = normalizeMethod(request.getMethod());
        String path = requestPath(request);

        // ログイン済みなら sys_permission を経由せず許可するパス
        if (isAuthenticatedOnlyPath(path)) {
            return new AuthorizationDecision(true);
        }

        List<String> matchedCodes = permissionRuleProvider.loadEnabledRules().stream()
                .filter(rule -> methodMatches(rule, method))
                .filter(rule -> pathMatches(rule, path))
                .map(PermissionRule::code)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(code -> !code.isBlank())
                .toList();

        boolean granted = !matchedCodes.isEmpty()
                && matchedCodes.stream().anyMatch(code -> hasAuthority(current, code));
        return new AuthorizationDecision(granted);
    }

    private boolean isAuthenticatedOnlyPath(String path) {
        return AUTHENTICATED_ONLY_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private boolean methodMatches(PermissionRule rule, String method) {
        String ruleMethod = normalizeMethod(rule.method());
        return !ruleMethod.isBlank() && ruleMethod.equals(method);
    }

    private boolean pathMatches(PermissionRule rule, String path) {
        String rulePath = rule.path();
        return rulePath != null && !rulePath.isBlank() && pathMatcher.match(rulePath.trim(), path);
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority::equals);
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication instanceof AnonymousAuthenticationToken;
    }

    private String normalizeMethod(String method) {
        return method == null ? "" : method.trim().toUpperCase(Locale.ROOT);
    }

    private String requestPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (uri == null || uri.isBlank()) {
            return "/";
        }
        if (contextPath != null && !contextPath.isBlank() && uri.startsWith(contextPath)) {
            return uri.substring(contextPath.length());
        }
        return uri;
    }
}
