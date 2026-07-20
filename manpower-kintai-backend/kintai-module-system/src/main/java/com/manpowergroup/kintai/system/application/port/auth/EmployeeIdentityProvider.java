package com.manpowergroup.kintai.system.application.port.auth;

import com.manpowergroup.kintai.common.enums.Status;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Supplies employee identity data to the system authentication use cases without
 * making the system module depend on the employee module.
 */
public interface EmployeeIdentityProvider {

    Optional<LoginIdentity> findLoginIdentityByEmail(String email);

    Optional<EmployeeProfile> findEmployeeProfile(Long employeeId);

    void recordSuccessfulLogin(Long accountId, LocalDateTime loggedInAt);

    record LoginIdentity(
            Long employeeId,
            Long accountId,
            String passwordHash,
            Status employeeStatus,
            Status accountStatus,
            String displayName,
            String email
    ) {
    }

    record EmployeeProfile(
            Long employeeId,
            Long companyId,
            String employeeCode,
            String displayName,
            String email
    ) {
    }
}
