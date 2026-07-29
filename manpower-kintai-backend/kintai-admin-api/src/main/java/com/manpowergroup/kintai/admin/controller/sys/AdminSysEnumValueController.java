package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.EnumValueAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.response.EnumValueResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumValueCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumValueUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/enum-values")
@RequiredArgsConstructor
public class AdminSysEnumValueController {

    private final SysEnumValueService service;

    /**
     * 列挙型に一致する列挙値を一覧取得する。
     *
     * @param enumTypeCode 対象の列挙型コード
     * @return 列挙値レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<EnumValueResponse>> listByEnumType(@RequestParam String enumTypeCode) {
        return Result.ok(service.listByEnumTypeCode(enumTypeCode).stream().map(EnumValueAssembler::toResponse).toList());
    }

    /**
     * IDに対応する列挙値を取得する。
     *
     * @param id 対象の列挙値ID
     * @return 列挙値レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<EnumValueResponse> getById(@PathVariable Long id) {
        return Result.ok(EnumValueAssembler.toResponse(service.getById(id)));
    }

    /**
     * 列挙値を新規作成する。
     *
     * @param request 処理対象の列挙値作成リクエスト
     * @return 列挙値レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<EnumValueResponse> create(@RequestBody @Valid EnumValueCreateRequest request) {
        return Result.ok(EnumValueAssembler.toResponse(service.create(EnumValueAssembler.toCommand(request))));
    }

    /**
     * 列挙値を更新する。
     *
     * @param id 対象の列挙値ID
     * @param request 処理対象の列挙値更新リクエスト
     * @return 列挙値レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<EnumValueResponse> update(@PathVariable Long id, @RequestBody @Valid EnumValueUpdateRequest request) {
        return Result.ok(EnumValueAssembler.toResponse(service.update(id, EnumValueAssembler.toCommand(request))));
    }

    /**
     * 列挙値を有効化する。
     *
     * @param id 対象の列挙値ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * 列挙値を無効化する。
     *
     * @param id 対象の列挙値ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDの列挙値を削除する。
     *
     * @param id 対象の列挙値ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
