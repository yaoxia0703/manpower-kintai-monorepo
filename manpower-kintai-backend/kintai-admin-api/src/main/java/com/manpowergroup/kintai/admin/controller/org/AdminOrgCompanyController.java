package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.assembler.org.CompanyAssembler;
import com.manpowergroup.kintai.employee.application.dto.org.response.CompanyResponse;
import com.manpowergroup.kintai.employee.application.dto.org.request.CompanyCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.org.request.CompanyUpdateRequest;
import com.manpowergroup.kintai.employee.application.service.org.OrgCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/org/companies")
@RequiredArgsConstructor
public class AdminOrgCompanyController {

    private final OrgCompanyService service;

    /**
     * 指定された検索条件で会社をページング取得する。
     *
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた会社レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<PageResult<CompanyResponse>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(PageRequest.of(page, size)).map(CompanyAssembler::toResponse));
    }

    /**
     * 指定された条件に一致する有効な会社を一覧取得する。
     *
     * @return 会社レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping("/enabled")
    public Result<List<CompanyResponse>> listEnabled() {
        return Result.ok(service.listEnabled().stream().map(CompanyAssembler::toResponse).toList());
    }

    /**
     * IDに対応する会社を取得する。
     *
     * @param id 対象の会社ID
     * @return 会社レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<CompanyResponse> getById(@PathVariable Long id) {
        return Result.ok(CompanyAssembler.toResponse(service.getById(id)));
    }

    /**
     * 会社を新規作成する。
     *
     * @param request 処理対象の会社作成リクエスト
     * @return 会社レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<CompanyResponse> create(@RequestBody @Valid CompanyCreateRequest request) {
        return Result.ok(CompanyAssembler.toResponse(service.create(CompanyAssembler.toCommand(request))));
    }

    /**
     * 会社を更新する。
     *
     * @param id 対象の会社ID
     * @param request 処理対象の会社更新リクエスト
     * @return 会社レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<CompanyResponse> update(@PathVariable Long id, @RequestBody @Valid CompanyUpdateRequest request) {
        return Result.ok(CompanyAssembler.toResponse(service.update(id, CompanyAssembler.toCommand(request))));
    }

    /**
     * 会社を有効化する。
     *
     * @param id 対象の会社ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * 会社を無効化する。
     *
     * @param id 対象の会社ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDの会社を削除する。
     *
     * @param id 対象の会社ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
