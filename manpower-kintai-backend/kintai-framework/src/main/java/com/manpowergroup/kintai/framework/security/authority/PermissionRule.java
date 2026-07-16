package com.manpowergroup.kintai.framework.security.authority;

import com.manpowergroup.kintai.common.enums.PermissionHttpMethod;

public record PermissionRule(String code, PermissionHttpMethod method, String path) {
}
