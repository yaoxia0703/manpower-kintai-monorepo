package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

import java.util.List;

public interface SysEmployeeRoleRepository {

    /**
     * 社員に一致する社員ロール割当を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員ロール割当一覧。該当しない場合は空リスト
     */
    List<SysEmployeeRole> listByEmployee(Long employeeId);

    /**
     * IDに対応する社員ロール割当を取得する。
     *
     * @param id 対象の社員ロール割当ID
     * @return 取得した社員ロール割当
     */
    SysEmployeeRole findById(Long id);

    /**
     * 社員およびロールおよび会社に一致する社員ロール割当が存在するか判定する。
     *
     * @param employeeId 対象の社員ID
     * @param roleId 対象のロールID
     * @param companyId 対象の会社ID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByEmployeeAndRoleAndCompany(Long employeeId, Long roleId, Long companyId);

    /**
     * 社員ロール割当を保存する。
     *
     * @param employeeRole 保存対象の社員ロール割当
     */
    void save(SysEmployeeRole employeeRole);

    /**
     * 社員ロール割当を更新する。
     *
     * @param employeeRole 保存対象の社員ロール割当
     */
    void updateById(SysEmployeeRole employeeRole);

    /**
     * 指定IDの社員ロール割当を削除する。
     *
     * @param id 対象の社員ロール割当ID
     */
    void deleteById(Long id);

    /**
     * IDに一致する社員ロール割当が存在するか判定する。
     *
     * @param id 対象の社員ロール割当ID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsById(Long id);

}
