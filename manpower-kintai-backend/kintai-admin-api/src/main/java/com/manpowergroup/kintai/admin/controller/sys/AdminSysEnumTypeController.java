package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.EnumTypeAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.response.EnumTypeResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumTypeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumTypeUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/enum-types")
@RequiredArgsConstructor
public class AdminSysEnumTypeController {

    private final SysEnumTypeService service;

    /**
     * 指定された検索条件で列挙型をページング取得する。
     *
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた列挙型レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<PageResult<EnumTypeResponse>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(PageRequest.of(page, size)).map(EnumTypeAssembler::toResponse));
    }

    /**
     * 指定された条件に一致する有効な列挙型を一覧取得する。
     *
     * @return 列挙型レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping("/enabled")
    public Result<List<EnumTypeResponse>> listEnabled() {
        return Result.ok(service.listEnabled().stream().map(EnumTypeAssembler::toResponse).toList());
    }

    /**
     * IDに対応する列挙型を取得する。
     *
     * @param id 対象の列挙型ID
     * @return 列挙型レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<EnumTypeResponse> getById(@PathVariable Long id) {
        return Result.ok(EnumTypeAssembler.toResponse(service.getById(id)));
    }

    /**
     * コードに対応する列挙型を取得する。
     *
     * @param code 対象コード
     * @return 列挙型レスポンスを含むAPIレスポンス
     */
    @GetMapping("/code/{code}")
    public Result<EnumTypeResponse> getByCode(@PathVariable String code) {
        return Result.ok(EnumTypeAssembler.toResponse(service.getByCode(code)));
    }

    /**
     * 列挙型を新規作成する。
     *
     * @param request 処理対象の列挙型作成リクエスト
     * @return 列挙型レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<EnumTypeResponse> create(@RequestBody @Valid EnumTypeCreateRequest request) {
        return Result.ok(EnumTypeAssembler.toResponse(service.create(EnumTypeAssembler.toCommand(request))));
    }

    /**
     * 列挙型を更新する。
     *
     * @param id 対象の列挙型ID
     * @param request 処理対象の列挙型更新リクエスト
     * @return 列挙型レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<EnumTypeResponse> update(@PathVariable Long id, @RequestBody @Valid EnumTypeUpdateRequest request) {
        return Result.ok(EnumTypeAssembler.toResponse(service.update(id, EnumTypeAssembler.toCommand(request))));
    }

    /**
     * 列挙型を有効化する。
     *
     * @param id 対象の列挙型ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * 列挙型を無効化する。
     *
     * @param id 対象の列挙型ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDの列挙型を削除する。
     *
     * @param id 対象の列挙型ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
