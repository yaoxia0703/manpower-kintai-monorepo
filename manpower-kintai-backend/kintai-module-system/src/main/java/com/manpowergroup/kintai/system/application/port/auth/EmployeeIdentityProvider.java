package com.manpowergroup.kintai.system.application.port.auth;

import com.manpowergroup.kintai.common.enums.Status;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Supplies employee identity data to the system authentication use cases without
 * making the system module depend on the employee module.
 */
public interface EmployeeIdentityProvider {

    /**
     * メールアドレスに対応するログインID情報を取得する。
     *
     * @param email 対象のメールアドレス
     * @return 対象が存在する場合はログインID情報を含むOptional、存在しない場合はOptional.empty()
     */
    Optional<LoginIdentity> findLoginIdentityByEmail(String email);

    /**
     * 指定社員のプロフィール情報を取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 対象が存在する場合は社員Profileを含むOptional、存在しない場合はOptional.empty()
     */
    Optional<EmployeeProfile> findEmployeeProfile(Long employeeId);

    /**
     * ログイン成功日時を社員アカウントへ記録する。
     *
     * @param accountId 対象の社員アカウントID
     * @param loggedInAt ログイン日時
     */
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
