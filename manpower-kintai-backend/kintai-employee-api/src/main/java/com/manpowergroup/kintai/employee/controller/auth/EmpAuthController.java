package com.manpowergroup.kintai.employee.controller.auth;

import com.manpowergroup.kintai.common.dto.auth.LoginRequest;
import com.manpowergroup.kintai.common.dto.auth.LoginResponse;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 認証Controller（社員向けログインAPI）
@RestController
@RequestMapping("/api/system/auth")
@RequiredArgsConstructor
public class EmpAuthController {

    private final AuthService authService;

    // ログイン
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return Result.ok(authService.login(request));
    }
}
