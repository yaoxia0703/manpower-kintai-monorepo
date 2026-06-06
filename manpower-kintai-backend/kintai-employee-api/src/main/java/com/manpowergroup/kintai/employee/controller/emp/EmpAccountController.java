package com.manpowergroup.kintai.employee.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.application.dto.emp.response.AccountResponse;
import com.manpowergroup.kintai.system.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
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

    // ログイン中の社員自身のアカウント情報を取得
    @GetMapping
    public Result<AccountResponse> getMyAccount(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(AccountResponse.from(service.getByEmployeeId(principal.employeeId())));
    }

    // パスワードを変更
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


