package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 権限マスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/sys/permissions")
@RequiredArgsConstructor
public class AdminSysPermissionController {

    private final SysPermissionService service;

    // 権限一覧をページング取得
    @GetMapping
    public Result<PageResult<SysPermission>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(PageRequest.of(page, size)));
    }

    // メニューIDで権限一覧を取得
    @GetMapping("/by-menu/{menuId}")
    public Result<List<SysPermission>> listByMenu(@PathVariable Long menuId) {
        return Result.ok(service.listByMenu(menuId));
    }

    // IDで権限を取得
    @GetMapping("/{id}")
    public Result<SysPermission> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // 権限を新規作成
    @PostMapping
    public Result<SysPermission> create(@RequestBody SysPermission permission) {
        return Result.ok(service.create(permission));
    }

    // 権限を更新
    @PutMapping("/{id}")
    public Result<SysPermission> update(@PathVariable Long id, @RequestBody SysPermission permission) {
        return Result.ok(service.update(id, permission));
    }

    // 権限を有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // 権限を無効化
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // 権限を削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


