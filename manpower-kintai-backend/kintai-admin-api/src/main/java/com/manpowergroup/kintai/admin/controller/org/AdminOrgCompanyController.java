package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.system.application.service.org.OrgCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 会社マスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/org/companies")
@RequiredArgsConstructor
public class AdminOrgCompanyController {

    private final OrgCompanyService service;

    // 会社一覧をページング取得
    @GetMapping
    public Result<PageResult<OrgCompany>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(PageRequest.of(page, size)));
    }

    // 有効な全会社を取得（ツリー構築用）
    @GetMapping("/enabled")
    public Result<List<OrgCompany>> listEnabled() {
        return Result.ok(service.listEnabled());
    }

    // IDで会社を取得
    @GetMapping("/{id}")
    public Result<OrgCompany> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // 会社を新規作成
    @PostMapping
    public Result<OrgCompany> create(@RequestBody OrgCompany company) {
        return Result.ok(service.create(company));
    }

    // 会社を更新
    @PutMapping("/{id}")
    public Result<OrgCompany> update(@PathVariable Long id, @RequestBody OrgCompany company) {
        return Result.ok(service.update(id, company));
    }

    // 会社を有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // 会社を無効化
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // 会社を削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


