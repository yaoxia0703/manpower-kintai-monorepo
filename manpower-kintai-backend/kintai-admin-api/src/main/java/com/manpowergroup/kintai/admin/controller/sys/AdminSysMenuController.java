package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.common.security.SecurityPermissions;
import com.manpowergroup.kintai.system.application.dto.sys.MenuRequest;
import com.manpowergroup.kintai.system.application.dto.sys.MenuResponse;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// メニューマスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/sys/menus")
@RequiredArgsConstructor
public class AdminSysMenuController {

    private final SysMenuService service;

    // 全メニューを取得（ツリー構築用）
    @GetMapping
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_READ)
    public Result<List<MenuResponse>> listAll() {
        return Result.ok(service.listAll().stream().map(MenuResponse::from).toList());
    }

    // IDでメニューを取得
    @GetMapping("/{id}")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_READ)
    public Result<MenuResponse> getById(@PathVariable Long id) {
        return Result.ok(MenuResponse.from(service.getById(id)));
    }

    // メニューを新規作成
    @PostMapping
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_WRITE)
    public Result<MenuResponse> create(@RequestBody @Valid MenuRequest request) {
        return Result.ok(MenuResponse.from(service.create(request.toEntity())));
    }

    // メニューを更新
    @PutMapping("/{id}")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_WRITE)
    public Result<MenuResponse> update(@PathVariable Long id, @RequestBody @Valid MenuRequest request) {
        return Result.ok(MenuResponse.from(service.update(id, request.toEntity())));
    }

    // メニューを表示
    @PutMapping("/{id}/show")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_WRITE)
    public Result<Void> show(@PathVariable Long id) {
        service.show(id);
        return Result.ok();
    }

    // メニューを非表示
    @PutMapping("/{id}/hide")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_WRITE)
    public Result<Void> hide(@PathVariable Long id) {
        service.hide(id);
        return Result.ok();
    }

    // メニューを有効化
    @PutMapping("/{id}/enable")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_WRITE)
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // メニューを無効化
    @PutMapping("/{id}/disable")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_WRITE)
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // メニューを削除（論理削除）
    @DeleteMapping("/{id}")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_MENU_WRITE)
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


