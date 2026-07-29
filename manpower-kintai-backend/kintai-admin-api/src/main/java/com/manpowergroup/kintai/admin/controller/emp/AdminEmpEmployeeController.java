package com.manpowergroup.kintai.admin.controller.emp;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.assembler.emp.EmployeeAssembler;
import com.manpowergroup.kintai.employee.application.dto.emp.response.EmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeeCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeeUpdateRequest;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 社員マスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/emp/employees")
@RequiredArgsConstructor
public class AdminEmpEmployeeController {

    private final EmpEmployeeService service;

    /**
     * 指定された検索条件で社員をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた社員レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<PageResult<EmployeeResponse>> page(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.pageByCompany(companyId, PageRequest.of(page, size)).map(EmployeeAssembler::toResponse));
    }

    /**
     * 指定された検索条件で社員をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param keyword 検索キーワード。未指定の場合は絞り込まない
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた社員レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping("/search")
    public Result<PageResult<EmployeeResponse>> search(
            @RequestParam Long companyId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.searchByName(companyId, keyword, PageRequest.of(page, size)).map(EmployeeAssembler::toResponse));
    }

    /**
     * IDに対応する社員を取得する。
     *
     * @param id 対象の社員ID
     * @return 社員レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<EmployeeResponse> getById(@PathVariable Long id) {
        return Result.ok(EmployeeAssembler.toResponse(service.getById(id)));
    }

    /**
     * 社員を新規作成する。
     *
     * @param request 処理対象の社員作成リクエスト
     * @return 社員レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<EmployeeResponse> create(@RequestBody @Valid EmployeeCreateRequest request) {
        return Result.ok(EmployeeAssembler.toResponse(service.create(EmployeeAssembler.toCommand(request))));
    }

    /**
     * 社員を更新する。
     *
     * @param id 対象の社員ID
     * @param request 処理対象の社員更新リクエスト
     * @return 社員レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<EmployeeResponse> update(@PathVariable Long id, @RequestBody @Valid EmployeeUpdateRequest request) {
        return Result.ok(EmployeeAssembler.toResponse(service.update(id, EmployeeAssembler.toCommand(request))));
    }

    /**
     * 社員を有効化する。
     *
     * @param id 対象の社員ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * 社員を無効化する。
     *
     * @param id 対象の社員ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDの社員を削除する。
     *
     * @param id 対象の社員ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


