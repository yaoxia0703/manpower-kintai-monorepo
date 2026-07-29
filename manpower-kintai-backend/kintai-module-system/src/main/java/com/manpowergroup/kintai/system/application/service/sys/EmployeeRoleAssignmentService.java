package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

public interface EmployeeRoleAssignmentService {

    /**
     * 社員ロール割当を割り当てる。
     *
     * @param command 処理対象の社員ロール割当割当コマンド
     * @return 社員ロール割当
     */
    SysEmployeeRole assign(EmployeeRoleAssignCommand command);

    /**
     * 社員ロール割当を更新する。
     *
     * @param id 対象の社員ロール割当ID
     * @param command 処理対象の社員ロール割当更新コマンド
     * @return 更新した社員ロール割当
     */
    SysEmployeeRole update(Long id, EmployeeRoleUpdateCommand command);

    /**
     * 社員ロール割当の割当を解除する。
     *
     * @param id 対象の社員ロール割当ID
     */
    void revoke(Long id);
}
