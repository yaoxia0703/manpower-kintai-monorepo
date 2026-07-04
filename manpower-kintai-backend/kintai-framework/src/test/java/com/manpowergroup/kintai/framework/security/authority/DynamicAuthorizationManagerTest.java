package com.manpowergroup.kintai.framework.security.authority;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DynamicAuthorizationManagerTest {

    @Test
    void deniesAnonymousUser() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() ->
                List.of(new PermissionRule("admin:employee:read", "GET", "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                () -> new AnonymousAuthenticationToken(
                        "anonymous",
                        "anonymousUser",
                        AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")),
                context("GET", "/admin/emp/employees/123"));

        assertFalse(decision.isGranted());
    }

    @Test
    void grantsSuperAdminWithoutLoadingRules() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() -> {
            throw new AssertionError("super admin should not need permission rules");
        });

        AuthorizationResult decision = manager.authorize(
                authenticated("ROLE_SUPER_ADMIN"),
                context("GET", "/admin/emp/employees/123"));

        assertTrue(decision.isGranted());
    }

    @Test
    void grantsWhenRequestMatchesRuleAndUserHasAuthority() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() ->
                List.of(new PermissionRule("admin:employee:read", "GET", "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:read"),
                context("GET", "/admin/emp/employees/123"));

        assertTrue(decision.isGranted());
    }

    @Test
    void deniesWhenRequestMatchesRuleButUserLacksAuthority() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() ->
                List.of(new PermissionRule("admin:employee:read", "GET", "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:update"),
                context("GET", "/admin/emp/employees/123"));

        assertFalse(decision.isGranted());
    }

    @Test
    void grantsWhenAnyMatchingRuleAuthorityIsPresent() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() -> List.of(
                new PermissionRule("admin:access", "GET", "/admin/**"),
                new PermissionRule("admin:employee:read", "GET", "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:read"),
                context("GET", "/admin/emp/employees/123"));

        assertTrue(decision.isGranted());
    }

    @Test
    void deniesWhenNoRuleMatchesMethodAndPath() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() ->
                List.of(new PermissionRule("admin:employee:read", "GET", "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:read"),
                context("POST", "/admin/emp/employees"));

        assertFalse(decision.isGranted());
    }

    private static Supplier<Authentication> authenticated(String... authorities) {
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(
                "user",
                null,
                AuthorityUtils.createAuthorityList(authorities));
        authentication.setAuthenticated(true);
        return () -> authentication;
    }

    private static RequestAuthorizationContext context(String method, String path) {
        HttpServletRequest request = new MockHttpServletRequest(method, path);
        return new RequestAuthorizationContext(request);
    }
}
