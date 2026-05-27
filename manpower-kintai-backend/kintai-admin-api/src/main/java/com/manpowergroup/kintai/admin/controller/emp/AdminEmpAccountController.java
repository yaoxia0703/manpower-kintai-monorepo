package com.manpowergroup.kintai.admin.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.system.application.service.emp.EmpAccountService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 社員アカウント管理Controller（管理者用）
@RestController
@RequestMapping("/admin/emp/accounts")
@RequiredArgsConstructor
public class AdminEmpAccountController {

    private final EmpAccountService service;

    // 社員IDでアカウントを取得
    @GetMapping("/by-employee/{employeeId}")
    public Result<EmpAccount> getByEmployeeId(@PathVariable Long employeeId) {
        return Result.ok(service.getByEmployeeId(employeeId));
    }

    // IDでアカウントを取得
    @GetMapping("/{id}")
    public Result<EmpAccount> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // アカウントを新規作成
    @PostMapping
    public Result<EmpAccount> create(@RequestBody CreateAccountRequest request) {
        return Result.ok(service.create(request.getAccount(), request.getPassword()));
    }

    // アカウント情報を更新
    @PutMapping("/{id}")
    public Result<EmpAccount> update(@PathVariable Long id, @RequestBody EmpAccount account) {
        return Result.ok(service.update(id, account));
    }

    // アカウントを有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // アカウントを無効化（ログイン停止）
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // アカウントを削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }

    @Data
    static class CreateAccountRequest {
        private EmpAccount account;
        private String password;
    }
}


