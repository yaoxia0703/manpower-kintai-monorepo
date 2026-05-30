package com.manpowergroup.kintai.system.application.service.auth;

import com.manpowergroup.kintai.common.dto.auth.AccessContext;

public interface AccessContextService {

    AccessContext load(Long employeeId, Long accountId);
}
