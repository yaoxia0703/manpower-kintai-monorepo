package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.EmployeeRoleResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EmployeeRoleAssignRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EmployeeRoleUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

public final class EmployeeRoleAssembler {

    private EmployeeRoleAssembler() {
    }

    /**
     * 入力データを社員ロール割当割当コマンドへ変換する。
     *
     * @param request 変換対象の社員ロール割当割当リクエスト
     * @return 変換後の社員ロール割当割当コマンド
     */
    public static EmployeeRoleAssignCommand toCommand(EmployeeRoleAssignRequest request) {
        return new EmployeeRoleAssignCommand(
                request.getEmployeeId(),
                request.getRoleId(),
                request.getCompanyId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    /**
     * 入力データを社員ロール割当更新コマンドへ変換する。
     *
     * @param request 変換対象の社員ロール割当更新リクエスト
     * @return 変換後の社員ロール割当更新コマンド
     */
    public static EmployeeRoleUpdateCommand toCommand(EmployeeRoleUpdateRequest request) {
        return new EmployeeRoleUpdateCommand(
                request.getEmployeeId(),
                request.getRoleId(),
                request.getCompanyId(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    /**
     * 入力データを社員ロール割当レスポンスへ変換する。
     *
     * @param employeeRole 保存対象の社員ロール割当
     * @return 変換後の社員ロール割当レスポンス
     */
    public static EmployeeRoleResponse toResponse(SysEmployeeRole employeeRole) {
        return EmployeeRoleResponse.from(employeeRole);
    }
}
