package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.application.service.org.OrgGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 職級マスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/org/grades")
@RequiredArgsConstructor
public class AdminOrgGradeController {

    private final OrgGradeService service;

    // 会社IDで職級一覧をページング取得
    @GetMapping
    public Result<PageResult<OrgGrade>> page(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.pageByCompany(companyId, PageRequest.of(page, size)));
    }

    // 会社IDで全職級を取得（ドロップダウン用）
    @GetMapping("/list")
    public Result<List<OrgGrade>> list(@RequestParam Long companyId) {
        return Result.ok(service.listByCompany(companyId));
    }

    // IDで職級を取得
    @GetMapping("/{id}")
    public Result<OrgGrade> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // 職級を新規作成
    @PostMapping
    public Result<OrgGrade> create(@RequestBody OrgGrade grade) {
        return Result.ok(service.create(grade));
    }

    // 職級を更新
    @PutMapping("/{id}")
    public Result<OrgGrade> update(@PathVariable Long id, @RequestBody OrgGrade grade) {
        return Result.ok(service.update(id, grade));
    }

    // 職級を有効化
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // 職級を無効化
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // 職級を削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


