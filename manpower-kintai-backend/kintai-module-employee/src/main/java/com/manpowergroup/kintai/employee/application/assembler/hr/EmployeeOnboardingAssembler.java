package com.manpowergroup.kintai.employee.application.assembler.hr;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.employee.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.employee.application.command.hr.EmployeeOnboardingCommand;
import com.manpowergroup.kintai.system.application.command.sys.EmployeeRoleAssignCommand;
import com.manpowergroup.kintai.employee.application.dto.hr.request.EmployeeOnboardingRequest;

import java.util.List;

public final class EmployeeOnboardingAssembler {

    private EmployeeOnboardingAssembler() {
    }

    /**
     * 入力データを社員作成コマンドへ変換する。
     *
     * @param command            変換対象の入社登録コマンド
     * @param operatorEmployeeId 操作を行う社員ID
     * @return 変換後の社員作成コマンド
     */
    public static EmployeeCreateCommand toEmployee(EmployeeOnboardingCommand command, Long operatorEmployeeId) {
        return new EmployeeCreateCommand(
            command.companyId(),
            command.employeeCode(),
            command.lastName(),
            command.firstName(),
            command.lastNameKana(),
            command.firstNameKana(),
            command.email(),
            command.phone(),
            command.gender(),
            null,
            command.hireDate(),
            null,
            Status.ENABLED
        );
    }

    /**
     * 入力データを社員アカウント作成コマンドへ変換する。
     *
     * @param command            変換対象の入社登録リクエスト
     * @param employeeId         対象の社員ID
     * @param operatorEmployeeId 操作を行う社員ID
     * @return 変換後の社員アカウント作成コマンド
     */
    public static AccountCreateCommand toAccount(EmployeeOnboardingCommand command, Long employeeId, Long operatorEmployeeId) {
        return new AccountCreateCommand(
            employeeId,
            command.username(),
            command.password()
        );
    }

    /**
     * 入力データを社員職位作成コマンドへ変換する。
     *
     * @param command            変換対象の入社登録リクエスト
     * @param employeeId         対象の社員ID
     * @param operatorEmployeeId 操作を行う社員ID
     * @return 変換後の社員職位作成コマンド
     */
    public static EmployeePositionCreateCommand toPosition(EmployeeOnboardingCommand command, Long employeeId, Long operatorEmployeeId) {
        return new EmployeePositionCreateCommand(
            employeeId,
            command.companyId(),
            command.nodeId(),
            command.gradeId(),
            1,
            command.hireDate(),
            null,
            Status.ENABLED
        );
    }

    /**
     * 入力データを社員ロール割当割当コマンドへ変換する。
     *
     * @param command            変換対象の入社登録リクエスト
     * @param employeeId         対象の社員ID
     * @param operatorEmployeeId 操作を行う社員ID
     * @return 社員ロール割当割当コマンド一覧。該当しない場合は空リスト
     */
    public static List<EmployeeRoleAssignCommand> toEmployeeRoles(EmployeeOnboardingCommand command, Long employeeId, Long operatorEmployeeId) {
        return command.roleIds().stream()
            .map(roleId -> new EmployeeRoleAssignCommand(
                employeeId,
                roleId,
                command.companyId(),
                command.hireDate(),
                null
            ))
            .toList();
    }
}
