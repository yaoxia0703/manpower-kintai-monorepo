package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;
import com.manpowergroup.kintai.system.application.service.sys.SysI18nService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 国際化翻訳管理Controller（管理者用）
@RestController
@RequestMapping("/admin/sys/i18n")
@RequiredArgsConstructor
public class AdminSysI18nController {

    private final SysI18nService service;

    // 参照タイプとレコードIDで翻訳一覧を取得
    @GetMapping
    public Result<List<SysI18n>> listByRef(
            @RequestParam String refType,
            @RequestParam Long refId) {
        return Result.ok(service.listByRef(refType, refId));
    }

    // IDで翻訳を取得
    @GetMapping("/{id}")
    public Result<SysI18n> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // 翻訳を保存（新規または上書き更新）
    @PostMapping
    public Result<SysI18n> upsert(@RequestBody SysI18n i18n) {
        return Result.ok(service.upsert(i18n));
    }

    // 翻訳を削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }

    // 参照レコードに紐づく全翻訳を削除
    @DeleteMapping("/by-ref")
    public Result<Void> removeByRef(
            @RequestParam String refType,
            @RequestParam Long refId) {
        service.removeByRef(refType, refId);
        return Result.ok();
    }
}


