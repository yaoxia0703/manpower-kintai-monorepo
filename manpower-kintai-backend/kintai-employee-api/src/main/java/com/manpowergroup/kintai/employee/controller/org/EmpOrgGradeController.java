package com.manpowergroup.kintai.employee.controller.org;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.application.service.org.OrgGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 職級マスタController（社員向け・参照のみ）
@RestController
@RequestMapping("/employee/org/grades")
@RequiredArgsConstructor
public class EmpOrgGradeController {

    private final OrgGradeService service;

    // 会社IDで職級一覧を取得（ドロップダウン用）
    @GetMapping
    public Result<List<OrgGrade>> list(@RequestParam Long companyId) {
        return Result.ok(service.listByCompany(companyId));
    }

    // IDで職級を取得
    @GetMapping("/{id}")
    public Result<OrgGrade> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }
}


