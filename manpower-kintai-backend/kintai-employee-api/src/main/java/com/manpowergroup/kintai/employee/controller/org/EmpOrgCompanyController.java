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

    // 有効な全会社を取得（組織選択用）
    @GetMapping
    public Result<List<OrgCompany>> listEnabled() {
        return Result.ok(service.listEnabled());
    }

    // IDで会社を取得
    @GetMapping("/{id}")
    public Result<OrgCompany> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }
}


