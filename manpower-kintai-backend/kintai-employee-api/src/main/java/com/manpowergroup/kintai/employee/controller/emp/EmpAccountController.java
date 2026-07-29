package com.manpowergroup.kintai.employee.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.application.dto.emp.response.AccountResponse;
import com.manpowergroup.kintai.employee.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// アカウントController（社員向け・自分自身のみ）
@RestController
@RequestMapping("/employee/emp/account")
@RequiredArgsConstructor
public class EmpAccountController {

    private final EmpAccountService service;

    /**
     * ログイン中の社員アカウントを取得する。
     *
     * @param principal ログインユーザー情報
     * @return 社員アカウントレスポンスを含むAPIレスポンス
     */
    @GetMapping
    public Result<AccountResponse> getMyAccount(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(AccountResponse.from(service.getByEmployeeId(principal.employeeId())));
    }

    /**
     * 社員アカウントの設定を変更する。
     *
     * @param principal ログインユーザー情報
     * @param request 処理対象のパスワード変更リクエスト
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/password")
    public Result<Void> changePassword(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestBody ChangePasswordRequest request) {
        EmpAccount account = service.getByEmployeeId(principal.employeeId());
        service.changePassword(account.getId(), request.getOldPassword(), request.getNewPassword());
        return Result.ok();
    }

    @Data
    static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;
    }
}


