package com.manpowergroup.kintai.employee.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 列挙値定義Controller（社員向け・参照のみ・ドロップダウン用）
@RestController
@RequestMapping("/employee/sys/enum-values")
@RequiredArgsConstructor
public class EmpSysEnumValueController {

    private final SysEnumValueService service;

    // 列挙型コードで列挙値一覧を取得
    @GetMapping
    public Result<List<SysEnumValue>> listByEnumType(@RequestParam String enumTypeCode) {
        return Result.ok(service.listByEnumTypeCode(enumTypeCode));
    }
}


