package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import lombok.RequiredArgsConstructor;
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
    public Result<List<SysMenu>> listAll() {
        return Result.ok(service.listAll());
    }

    // IDでメニューを取得
    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // メニューを新規作成
    @PostMapping
    public Result<SysMenu> create(@RequestBody SysMenu menu) {
        return Result.ok(service.create(menu));
    }

    // メニューを更新
    @PutMapping("/{id}")
    public Result<SysMenu> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        return Result.ok(service.update(id, menu));
    }

    // メニューを表示
    @PutMapping("/{id}/show")
    public Result<Void> show(@PathVariable Long id) {
        service.show(id);
        return Result.ok();
    }

    // メニューを非表示
    @PutMapping("/{id}/hide")
    public Result<Void> hide(@PathVariable Long id) {
        service.hide(id);
        return Result.ok();
    }

    // メニューを有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // メニューを無効化
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // メニューを削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


