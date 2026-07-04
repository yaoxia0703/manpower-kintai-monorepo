package com.manpowergroup.kintai.framework.security.authority;

import java.util.List;

public interface PermissionRuleProvider {

    List<PermissionRule> loadEnabledRules();
}
