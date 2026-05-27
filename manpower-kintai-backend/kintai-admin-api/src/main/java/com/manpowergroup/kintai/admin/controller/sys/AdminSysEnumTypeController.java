package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 列挙型マスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/sys/enum-types")
@RequiredArgsConstructor
public class AdminSysEnumTypeController {

    private final SysEnumTypeService service;

    // 列挙型一覧をページング取得
    @GetMapping
    public Result<PageResult<SysEnumType>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(PageRequest.of(page, size)));
    }

    // 有効な全列挙型を取得（ドロップダウン用）
    @GetMapping("/enabled")
    public Result<List<SysEnumType>> listEnabled() {
        return Result.ok(service.listEnabled());
    }

    // IDで列挙型を取得
    @GetMapping("/{id}")
    public Result<SysEnumType> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // コードで列挙型を取得
    @GetMapping("/code/{code}")
    public Result<SysEnumType> getByCode(@PathVariable String code) {
        return Result.ok(service.getByCode(code));
    }

    // 列挙型を新規作成
    @PostMapping
    public Result<SysEnumType> create(@RequestBody SysEnumType enumType) {
        return Result.ok(service.create(enumType));
    }

    // 列挙型を更新
    @PutMapping("/{id}")
    public Result<SysEnumType> update(@PathVariable Long id, @RequestBody SysEnumType enumType) {
        return Result.ok(service.update(id, enumType));
    }

    // 列挙型を有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // 列挙型を無効化
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // 列挙型を削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


