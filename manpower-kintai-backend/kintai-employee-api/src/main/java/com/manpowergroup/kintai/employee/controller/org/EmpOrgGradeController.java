package com.manpowergroup.kintai.employee.controller.org;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.employee.application.service.org.OrgGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 職級マスタController（社員向け・参照のみ）
@RestController
@RequestMapping("/employee/org/grades")
@RequiredArgsConstructor
public class EmpOrgGradeController {

    private final OrgGradeService service;

    /**
     * 指定された条件に一致する職級を一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 職級一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<OrgGrade>> list(@RequestParam Long companyId) {
        return Result.ok(service.listByCompany(companyId));
    }

    /**
     * IDに対応する職級を取得する。
     *
     * @param id 対象の職級ID
     * @return 職級を含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<OrgGrade> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }
}


