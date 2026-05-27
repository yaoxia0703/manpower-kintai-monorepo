package com.manpowergroup.kintai.employee.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// 社員プロフィールController（社員向け・自分自身のみ）
@RestController
@RequestMapping("/employee/emp/profile")
@RequiredArgsConstructor
public class EmpEmployeeController {

    private final EmpEmployeeService service;

    // ログイン中の社員自身のプロフィールを取得
    @GetMapping
    public Result<EmpEmployee> getMyProfile(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(service.getById(principal.employeeId()));
    }

    // ログイン中の社員自身の連絡先情報を更新（限定フィールドのみ）
    @PutMapping
    public Result<EmpEmployee> updateMyProfile(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestBody EmpEmployee employee) {
        return Result.ok(service.update(principal.employeeId(), employee));
    }
}


