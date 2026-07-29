package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

import java.util.List;

public interface SysEmployeeRoleService  {

    /**
     * IDに対応する社員ロール割当を取得する。
     *
     * @param id 対象の社員ロール割当ID
     * @return 取得した社員ロール割当
     */
    SysEmployeeRole getById(Long id);

    /**
     * 指定された条件に一致する有効な社員ロール割当を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員ロール割当一覧。該当しない場合は空リスト
     */
    List<SysEmployeeRole> listActiveByEmployee(Long employeeId);
}
