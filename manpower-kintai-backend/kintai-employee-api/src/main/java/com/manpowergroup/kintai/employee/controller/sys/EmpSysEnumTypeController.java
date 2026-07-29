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

    /**
     * 指定された条件に一致する有効な列挙型を一覧取得する。
     *
     * @return 列挙型一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<SysEnumType>> listEnabled() {
        return Result.ok(service.listEnabled());
    }

    /**
     * コードに対応する列挙型を取得する。
     *
     * @param code 対象コード
     * @return 列挙型を含むAPIレスポンス
     */
    @GetMapping("/code/{code}")
    public Result<SysEnumType> getByCode(@PathVariable String code) {
        return Result.ok(service.getByCode(code));
    }
}


