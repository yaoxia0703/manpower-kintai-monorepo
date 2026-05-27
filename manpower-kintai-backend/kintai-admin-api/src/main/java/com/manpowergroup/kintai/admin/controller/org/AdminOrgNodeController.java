package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.application.service.org.OrgNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 組織ノード管理Controller（管理者用）
@RestController
@RequestMapping("/admin/org/nodes")
@RequiredArgsConstructor
public class AdminOrgNodeController {

    private final OrgNodeService service;

    // 会社IDで組織ノード一覧をページング取得
    @GetMapping
    public Result<PageResult<OrgNode>> page(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.pageByCompany(companyId, PageRequest.of(page, size)));
    }

    // 会社IDで有効な全ノードを取得（ツリー構築用）
    @GetMapping("/tree")
    public Result<List<OrgNode>> listTree(@RequestParam Long companyId) {
        return Result.ok(service.listEnabledByCompany(companyId));
    }

    // IDで組織ノードを取得
    @GetMapping("/{id}")
    public Result<OrgNode> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // 組織ノードを新規作成
    @PostMapping
    public Result<OrgNode> create(@RequestBody OrgNode node) {
        return Result.ok(service.create(node));
    }

    // 組織ノードを更新
    @PutMapping("/{id}")
    public Result<OrgNode> update(@PathVariable Long id, @RequestBody OrgNode node) {
        return Result.ok(service.update(id, node));
    }

    // 組織ノードを有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // 組織ノードを無効化
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // 組織ノードを削除（論理削除 + Closure Table削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


