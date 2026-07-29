package com.manpowergroup.kintai.employee.controller.org;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.employee.application.service.org.OrgCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 会社マスタController（社員向け・参照のみ）
@RestController
@RequestMapping("/employee/org/companies")
@RequiredArgsConstructor
public class EmpOrgCompanyController {

    private final OrgCompanyService service;

    /**
     * 指定された条件に一致する有効な会社を一覧取得する。
     *
     * @return 会社一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<OrgCompany>> listEnabled() {
        return Result.ok(service.listEnabled());
    }

    /**
     * IDに対応する会社を取得する。
     *
     * @param id 対象の会社ID
     * @return 会社を含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<OrgCompany> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }
}


