package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ロールマスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/sys/roles")
@RequiredArgsConstructor
public class AdminSysRoleController {

    private final SysRoleService service;

    // ロール一覧をページング取得
    @GetMapping
    public Result<PageResult<SysRole>> page(
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(companyId, PageRequest.of(page, size)));
    }

    // 会社IDでロール一覧を全取得（ドロップダウン用）
    @GetMapping("/list")
    public Result<List<SysRole>> list(@RequestParam(required = false) Long companyId) {
        return Result.ok(service.listByCompany(companyId));
    }

    // IDでロールを取得
    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // ロールを新規作成
    @PostMapping
    public Result<SysRole> create(@RequestBody SysRole role) {
        return Result.ok(service.create(role));
    }

    // ロールを更新
    @PutMapping("/{id}")
    public Result<SysRole> update(@PathVariable Long id, @RequestBody SysRole role) {
        return Result.ok(service.update(id, role));
    }

    // ロールにメニューを割り当て
    @PutMapping("/{id}/menus")
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody AssignRequest request) {
        service.assignMenus(id, request.getIds());
        return Result.ok();
    }

    // ロールに権限を割り当て
    @PutMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody AssignRequest request) {
        service.assignPermissions(id, request.getIds());
        return Result.ok();
    }

    // ロールを有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // ロールを無効化
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // ロールを削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }

    @Data
    static class AssignRequest {
        private List<Long> ids;
    }
}


