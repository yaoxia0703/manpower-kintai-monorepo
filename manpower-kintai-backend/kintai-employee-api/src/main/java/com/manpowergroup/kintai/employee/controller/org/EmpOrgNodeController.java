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

    // 会社IDで有効な全組織ノードを取得（組織ツリー表示用）
    @GetMapping
    public Result<List<OrgNode>> listEnabled(@RequestParam Long companyId) {
        return Result.ok(service.listEnabledByCompany(companyId));
    }

    // IDで組織ノードを取得
    @GetMapping("/{id}")
    public Result<OrgNode> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }
}


