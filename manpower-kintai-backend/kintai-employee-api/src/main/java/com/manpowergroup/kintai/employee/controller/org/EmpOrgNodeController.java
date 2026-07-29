package com.manpowergroup.kintai.employee.controller.org;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.employee.application.service.org.OrgNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 組織ノードController（社員向け・参照のみ）
@RestController
@RequestMapping("/employee/org/nodes")
@RequiredArgsConstructor
public class EmpOrgNodeController {

    private final OrgNodeService service;

    /**
     * 指定された条件に一致する有効な組織ノードを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 組織ノード一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<OrgNode>> listEnabled(@RequestParam Long companyId) {
        return Result.ok(service.listEnabledByCompany(companyId));
    }

    /**
     * IDに対応する組織ノードを取得する。
     *
     * @param id 対象の組織ノードID
     * @return 組織ノードを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<OrgNode> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }
}


