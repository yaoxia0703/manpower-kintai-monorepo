package com.manpowergroup.kintai.framework.security.authority;

import java.util.List;

// 権限コードをロードするインターフェース（system モジュールで実装）
public interface EmployeeAuthorityProvider {
    List<String> loadPermissionCodes(Long employeeId);
}