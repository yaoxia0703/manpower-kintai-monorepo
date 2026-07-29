package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.assembler.org.NodeAssembler;
import com.manpowergroup.kintai.employee.application.dto.org.response.NodeResponse;
import com.manpowergroup.kintai.employee.application.dto.org.request.NodeCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.org.request.NodeUpdateRequest;
import com.manpowergroup.kintai.employee.application.service.org.OrgNodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/org/nodes")
@RequiredArgsConstructor
public class AdminOrgNodeController {

    private final OrgNodeService service;

    /**
     * 指定された検索条件で組織ノードをページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた組織ノードレスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<PageResult<NodeResponse>> page(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.pageByCompany(companyId, PageRequest.of(page, size)).map(NodeAssembler::toResponse));
    }

    /**
     * 組織ノードを階層構造で一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 組織ノードレスポンス一覧を含むAPIレスポンス
     */
    @GetMapping("/tree")
    public Result<List<NodeResponse>> listTree(@RequestParam Long companyId) {
        return Result.ok(service.listEnabledByCompany(companyId).stream().map(NodeAssembler::toResponse).toList());
    }

    /**
     * IDに対応する組織ノードを取得する。
     *
     * @param id 対象の組織ノードID
     * @return 組織ノードレスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<NodeResponse> getById(@PathVariable Long id) {
        return Result.ok(NodeAssembler.toResponse(service.getById(id)));
    }

    /**
     * 組織ノードを新規作成する。
     *
     * @param request 処理対象の組織ノード作成リクエスト
     * @return 組織ノードレスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<NodeResponse> create(@RequestBody @Valid NodeCreateRequest request) {
        return Result.ok(NodeAssembler.toResponse(service.create(NodeAssembler.toCommand(request))));
    }

    /**
     * 組織ノードを更新する。
     *
     * @param id 対象の組織ノードID
     * @param request 処理対象の組織ノード更新リクエスト
     * @return 組織ノードレスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<NodeResponse> update(@PathVariable Long id, @RequestBody @Valid NodeUpdateRequest request) {
        return Result.ok(NodeAssembler.toResponse(service.update(id, NodeAssembler.toCommand(request))));
    }

    /**
     * 組織ノードを有効化する。
     *
     * @param id 対象の組織ノードID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * 組織ノードを無効化する。
     *
     * @param id 対象の組織ノードID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDの組織ノードを削除する。
     *
     * @param id 対象の組織ノードID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
