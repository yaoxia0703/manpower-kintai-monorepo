package com.manpowergroup.kintai.employee.application.assembler.emp;

import com.manpowergroup.kintai.employee.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.AccountUpdateCommand;
import com.manpowergroup.kintai.employee.application.dto.emp.response.AccountResponse;
import com.manpowergroup.kintai.employee.application.dto.emp.request.AccountCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.emp.request.AccountUpdateRequest;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;

public final class AccountAssembler {

    private AccountAssembler() {
    }

    /**
     * 入力データを社員アカウント作成コマンドへ変換する。
     *
     * @param request 変換対象の社員アカウント作成リクエスト
     * @return 変換後の社員アカウント作成コマンド
     */
    public static AccountCreateCommand toCommand(AccountCreateRequest request) {
        return new AccountCreateCommand(
                request.getEmployeeId(),
                request.getUsername(),
                request.getPassword()
        );
    }

    /**
     * 入力データを社員アカウント更新コマンドへ変換する。
     *
     * @param request 変換対象の社員アカウント更新リクエスト
     * @return 変換後の社員アカウント更新コマンド
     */
    public static AccountUpdateCommand toCommand(AccountUpdateRequest request) {
        return new AccountUpdateCommand(request.getUsername());
    }

    /**
     * 入力データを社員アカウントレスポンスへ変換する。
     *
     * @param account 処理対象の社員アカウント
     * @return 変換後の社員アカウントレスポンス
     */
    public static AccountResponse toResponse(EmpAccount account) {
        return AccountResponse.from(account);
    }
}
