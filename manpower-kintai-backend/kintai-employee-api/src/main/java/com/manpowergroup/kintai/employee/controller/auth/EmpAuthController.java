package com.manpowergroup.kintai.employee.controller.auth;

import com.manpowergroup.kintai.common.dto.auth.CurrentUserResponse;
import com.manpowergroup.kintai.common.dto.auth.LoginRequest;
import com.manpowergroup.kintai.common.dto.auth.LoginResponse;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.application.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/auth")
@RequiredArgsConstructor
public class EmpAuthController {

    private final AuthService authService;

    /**
     * 認証情報を検証し、ログイン処理を実行する。
     *
     * @param request 処理対象のログインリクエスト
     * @return Loginレスポンスを含むAPIレスポンス
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return Result.ok(authService.login(request));
    }

    /**
     * ログイン中のユーザーをログアウトする。
     *
     * @return 処理結果を示すAPIレスポンス
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.ok();
    }

    /**
     * ログイン中のユーザー情報を取得する。
     *
     * @param principal ログインユーザー情報
     * @return CurrentUserレスポンスを含むAPIレスポンス
     */
    @GetMapping("/me")
    public Result<CurrentUserResponse> me(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(authService.me(principal.employeeId(), principal.accountId()));
    }
}
