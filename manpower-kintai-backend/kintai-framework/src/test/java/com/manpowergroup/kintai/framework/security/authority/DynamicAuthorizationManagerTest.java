package com.manpowergroup.kintai.framework.security.authority;

import com.manpowergroup.kintai.common.enums.PermissionHttpMethod;
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
                List.of(new PermissionRule("admin:employee:read", PermissionHttpMethod.GET, "/admin/emp/employees/**")));

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
                List.of(new PermissionRule("admin:employee:read", PermissionHttpMethod.GET, "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:read"),
                context("GET", "/admin/emp/employees/123"));

        assertTrue(decision.isGranted());
    }

    @Test
    void deniesWhenRequestMatchesRuleButUserLacksAuthority() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() ->
                List.of(new PermissionRule("admin:employee:read", PermissionHttpMethod.GET, "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:update"),
                context("GET", "/admin/emp/employees/123"));

        assertFalse(decision.isGranted());
    }

    @Test
    void grantsWhenAnyMatchingRuleAuthorityIsPresent() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() -> List.of(
                new PermissionRule("admin:access", PermissionHttpMethod.GET, "/admin/**"),
                new PermissionRule("admin:employee:read", PermissionHttpMethod.GET, "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:read"),
                context("GET", "/admin/emp/employees/123"));

        assertTrue(decision.isGranted());
    }

    @Test
    void deniesWhenNoRuleMatchesMethodAndPath() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() ->
                List.of(new PermissionRule("admin:employee:read", PermissionHttpMethod.GET, "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:read"),
                context("POST", "/admin/emp/employees"));

        assertFalse(decision.isGranted());
    }

    @Test
    void deniesUnknownRequestMethodWithoutThrowing() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() ->
                List.of(
                        new PermissionRule(
                                "admin:employee:read", PermissionHttpMethod.GET,
                                "/admin/emp/employees/**"),
                        new PermissionRule(
                                "admin:employee:read", null,
                                "/admin/emp/employees/**")));

        AuthorizationResult decision = manager.authorize(
                authenticated("admin:employee:read"),
                context("CUSTOM", "/admin/emp/employees/123"));

        assertFalse(decision.isGranted());
    }

    @Test
    void grantsAuthenticatedOnlyAccessWithoutPermissionRule() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(List::of);

        assertTrue(manager.authorize(
                authenticated("ROLE_EMPLOYEE"),
                context("GET", "/api/system/auth/me")).isGranted());
        assertTrue(manager.authorize(
                authenticated("ROLE_EMPLOYEE"),
                context("POST", "/api/system/auth/logout")).isGranted());
        assertTrue(manager.authorize(
                authenticated("ROLE_EMPLOYEE"),
                context("GET", "/employee/notifications/unread-count")).isGranted());
        assertFalse(manager.authorize(
                authenticated("ROLE_EMPLOYEE"),
                context("POST", "/employee/att/requests")).isGranted());
        assertFalse(manager.authorize(
                authenticated("ROLE_EMPLOYEE"),
                context("GET", "/employee/approvals/pending")).isGranted());
    }

    @Test
    void grantsRequestAndApprovalEndpointsOnlyWithMatchingAuthority() {
        DynamicAuthorizationManager manager = new DynamicAuthorizationManager(() -> List.of(
                new PermissionRule("employee:request:create", PermissionHttpMethod.POST, "/employee/att/requests"),
                new PermissionRule("manager:approval:read", PermissionHttpMethod.GET, "/employee/approvals/**")));

        assertTrue(manager.authorize(
                authenticated("employee:request:create"),
                context("POST", "/employee/att/requests")).isGranted());
        assertFalse(manager.authorize(
                authenticated("employee:request:create"),
                context("GET", "/employee/approvals/pending")).isGranted());
        assertTrue(manager.authorize(
                authenticated("manager:approval:read"),
                context("GET", "/employee/approvals/pending")).isGranted());
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
