package com.manpowergroup.kintai.employee.domain.repository.emp;

import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;

public interface EmpAccountRepository {
    /**
     * IDに対応する社員アカウントを取得する。
     *
     * @param id 対象の社員アカウントID
     * @return 取得した社員アカウント
     */
    EmpAccount getById(Long id);

    /**
     * 社員IDに対応する社員アカウントを取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 取得した社員アカウント
     */
    EmpAccount getByEmployeeId(Long employeeId);

    /**
     * ユーザー名に一致する社員アカウントの件数を取得する。
     *
     * @param username 対象のユーザー名
     * @return 該当件数
     */
    long countByUsername(String username);

    /**
     * 指定IDを除き、同一条件の社員アカウントが存在するか判定する。
     *
     * @param username 対象のユーザー名
     * @param excludeId 重複確認から除外するID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByUsernameExcludingId(String username, Long excludeId);

    /**
     * 社員アカウントを保存する。
     *
     * @param empAccount 保存対象の社員アカウント
     */
    void save(EmpAccount empAccount);

    /**
     * 社員アカウントを更新する。
     *
     * @param empAccount 保存対象の社員アカウント
     */
    void updateById(EmpAccount empAccount);

    /**
     * 指定IDの社員アカウントを削除する。
     *
     * @param id 対象の社員アカウントID
     */
    void deleteById(Long id);
}
