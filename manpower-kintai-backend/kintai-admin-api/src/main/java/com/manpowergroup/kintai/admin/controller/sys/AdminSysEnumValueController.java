package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 列挙値定義管理Controller（管理者用）
@RestController
@RequestMapping("/admin/sys/enum-values")
@RequiredArgsConstructor
public class AdminSysEnumValueController {

    private final SysEnumValueService service;

    // 列挙型コードで列挙値一覧を取得
    @GetMapping
    public Result<List<SysEnumValue>> listByEnumType(@RequestParam String enumTypeCode) {
        return Result.ok(service.listByEnumTypeCode(enumTypeCode));
    }

    // IDで列挙値を取得
    @GetMapping("/{id}")
    public Result<SysEnumValue> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // 列挙値を新規作成
    @PostMapping
    public Result<SysEnumValue> create(@RequestBody SysEnumValue enumValue) {
        return Result.ok(service.create(enumValue));
    }

    // 列挙値を更新
    @PutMapping("/{id}")
    public Result<SysEnumValue> update(@PathVariable Long id, @RequestBody SysEnumValue enumValue) {
        return Result.ok(service.update(id, enumValue));
    }

    // 列挙値を有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // 列挙値を無効化
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // 列挙値を削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


