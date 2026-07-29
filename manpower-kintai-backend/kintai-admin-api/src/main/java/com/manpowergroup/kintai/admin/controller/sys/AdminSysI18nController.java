package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.I18nAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.response.I18nResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.I18nUpsertRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysI18nService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/i18n")
@RequiredArgsConstructor
public class AdminSysI18nController {

    private final SysI18nService service;

    /**
     * 参照情報に一致する多言語情報を一覧取得する。
     *
     * @param refType 関連データの種別
     * @param refId 関連データのID
     * @return 多言語情報レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<I18nResponse>> listByRef(
            @RequestParam String refType,
            @RequestParam Long refId) {
        return Result.ok(service.listByRef(refType, refId).stream().map(I18nAssembler::toResponse).toList());
    }

    /**
     * IDに対応する多言語情報を取得する。
     *
     * @param id 対象の多言語情報ID
     * @return 多言語情報レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<I18nResponse> getById(@PathVariable Long id) {
        return Result.ok(I18nAssembler.toResponse(service.getById(id)));
    }

    /**
     * 多言語情報を登録または更新する。
     *
     * @param request 処理対象の多言語情報Upsertリクエスト
     * @return 多言語情報レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<I18nResponse> upsert(@RequestBody @Valid I18nUpsertRequest request) {
        return Result.ok(I18nAssembler.toResponse(service.upsert(I18nAssembler.toCommand(request))));
    }

    /**
     * 指定IDの多言語情報を削除する。
     *
     * @param id 対象の多言語情報ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }

    /**
     * 参照情報に対応する多言語情報を削除する。
     *
     * @param refType 関連データの種別
     * @param refId 関連データのID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/by-ref")
    public Result<Void> removeByRef(
            @RequestParam String refType,
            @RequestParam Long refId) {
        service.removeByRef(refType, refId);
        return Result.ok();
    }
}
