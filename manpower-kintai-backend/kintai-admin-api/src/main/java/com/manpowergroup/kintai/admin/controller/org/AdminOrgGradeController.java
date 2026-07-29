package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.assembler.org.GradeAssembler;
import com.manpowergroup.kintai.employee.application.dto.org.response.GradeResponse;
import com.manpowergroup.kintai.employee.application.dto.org.request.GradeCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.org.request.GradeUpdateRequest;
import com.manpowergroup.kintai.employee.application.service.org.OrgGradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/org/grades")
@RequiredArgsConstructor
public class AdminOrgGradeController {

    private final OrgGradeService service;

    /**
     * 指定された検索条件で職級をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた職級レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<PageResult<GradeResponse>> page(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.pageByCompany(companyId, PageRequest.of(page, size)).map(GradeAssembler::toResponse));
    }

    /**
     * 指定された条件に一致する職級を一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 職級レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping("/list")
    public Result<List<GradeResponse>> list(@RequestParam Long companyId) {
        return Result.ok(service.listByCompany(companyId).stream().map(GradeAssembler::toResponse).toList());
    }

    /**
     * IDに対応する職級を取得する。
     *
     * @param id 対象の職級ID
     * @return 職級レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<GradeResponse> getById(@PathVariable Long id) {
        return Result.ok(GradeAssembler.toResponse(service.getById(id)));
    }

    /**
     * 職級を新規作成する。
     *
     * @param request 処理対象の職級作成リクエスト
     * @return 職級レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<GradeResponse> create(@RequestBody @Valid GradeCreateRequest request) {
        return Result.ok(GradeAssembler.toResponse(service.create(GradeAssembler.toCommand(request))));
    }

    /**
     * 職級を更新する。
     *
     * @param id 対象の職級ID
     * @param request 処理対象の職級更新リクエスト
     * @return 職級レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<GradeResponse> update(@PathVariable Long id, @RequestBody @Valid GradeUpdateRequest request) {
        return Result.ok(GradeAssembler.toResponse(service.update(id, GradeAssembler.toCommand(request))));
    }

    /**
     * 職級を有効化する。
     *
     * @param id 対象の職級ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * 職級を無効化する。
     *
     * @param id 対象の職級ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDの職級を削除する。
     *
     * @param id 対象の職級ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
