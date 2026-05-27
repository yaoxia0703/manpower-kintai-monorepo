package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.application.service.sys.SysEmployeeRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 社員ロール関連管理Controller（管理者用）
@RestController
@RequestMapping("/admin/sys/employee-roles")
@RequiredArgsConstructor
public class AdminSysEmployeeRoleController {

    private final SysEmployeeRoleService service;

    // 社員IDで有効なロール一覧を取得
    @GetMapping
    public Result<List<SysEmployeeRole>> listActive(@RequestParam Long employeeId) {
        return Result.ok(service.listActiveByEmployee(employeeId));
    }

    // IDで社員ロールを取得
    @GetMapping("/{id}")
    public Result<SysEmployeeRole> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // 社員にロールを付与
    @PostMapping
    public Result<SysEmployeeRole> assign(@RequestBody SysEmployeeRole employeeRole) {
        return Result.ok(service.assign(employeeRole));
    }

    // 社員ロールの有効期間を更新
    @PutMapping("/{id}")
    public Result<SysEmployeeRole> update(@PathVariable Long id, @RequestBody SysEmployeeRole employeeRole) {
        return Result.ok(service.update(id, employeeRole));
    }

    // 社員からロールを剥奪（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> revoke(@PathVariable Long id) {
        service.revoke(id);
        return Result.ok();
    }
}


