package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.PermissionAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.response.PermissionResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.PermissionCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.PermissionUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sys/permissions")
@RequiredArgsConstructor
public class AdminSysPermissionController {

    private final SysPermissionService service;

    /**
     * 指定された検索条件で権限をページング取得する。
     *
     * @param menuId 対象のメニューID
     * @param keyword 検索キーワード。未指定の場合は絞り込まない
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた権限レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<PageResult<PermissionResponse>> page(
            @RequestParam(required = false) Long menuId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(menuId, keyword, PageRequest.of(page, size))
                .map(PermissionAssembler::toResponse));
    }

    /**
     * IDに対応する権限を取得する。
     *
     * @param id 対象の権限ID
     * @return 権限レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<PermissionResponse> getById(@PathVariable Long id) {
        return Result.ok(PermissionAssembler.toResponse(service.getById(id)));
    }

    /**
     * 権限を新規作成する。
     *
     * @param request 処理対象の権限作成リクエスト
     * @return 権限レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<PermissionResponse> create(@RequestBody @Valid PermissionCreateRequest request) {
        return Result.ok(PermissionAssembler.toResponse(service.create(PermissionAssembler.toCommand(request))));
    }

    /**
     * 権限を更新する。
     *
     * @param id 対象の権限ID
     * @param request 処理対象の権限更新リクエスト
     * @return 権限レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<PermissionResponse> update(@PathVariable Long id, @RequestBody @Valid PermissionUpdateRequest request) {
        return Result.ok(PermissionAssembler.toResponse(service.update(id, PermissionAssembler.toCommand(request))));
    }

    /**
     * 権限を有効化する。
     *
     * @param id 対象の権限ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * 権限を無効化する。
     *
     * @param id 対象の権限ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDの権限を削除する。
     *
     * @param id 対象の権限ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
