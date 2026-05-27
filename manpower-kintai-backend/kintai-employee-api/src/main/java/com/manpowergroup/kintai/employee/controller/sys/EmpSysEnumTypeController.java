package com.manpowergroup.kintai.employee.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 列挙型マスタController（社員向け・参照のみ・ドロップダウン用）
@RestController
@RequestMapping("/employee/sys/enum-types")
@RequiredArgsConstructor
public class EmpSysEnumTypeController {

    private final SysEnumTypeService service;

    // 有効な全列挙型を取得
    @GetMapping
    public Result<List<SysEnumType>> listEnabled() {
        return Result.ok(service.listEnabled());
    }

    // コードで列挙型を取得
    @GetMapping("/code/{code}")
    public Result<SysEnumType> getByCode(@PathVariable String code) {
        return Result.ok(service.getByCode(code));
    }
}


