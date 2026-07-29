package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.RoleAuthorizationAssembler;
import com.manpowergroup.kintai.system.application.assembler.sys.RoleAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.response.RoleAuthorizationResponse;
import com.manpowergroup.kintai.system.application.dto.sys.response.RoleResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleAssignRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleAuthorizationSaveRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.RoleAuthorizationService;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
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

import java.util.List;

@RestController
@RequestMapping("/admin/sys/roles")
@RequiredArgsConstructor
public class AdminSysRoleController {

    private final SysRoleService service;
    private final RoleAuthorizationService authorizationService;

    /**
     * 指定された検索条件でロールをページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされたロールレスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<PageResult<RoleResponse>> page(
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(companyId, PageRequest.of(page, size)).map(RoleAssembler::toResponse));
    }

    /**
     * 指定された条件に一致するロールを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return ロールレスポンス一覧を含むAPIレスポンス
     */
    @GetMapping("/list")
    public Result<List<RoleResponse>> list(@RequestParam(required = false) Long companyId) {
        return Result.ok(service.listByCompany(companyId).stream().map(RoleAssembler::toResponse).toList());
    }

    /**
     * IDに対応するロールを取得する。
     *
     * @param id 対象のロールID
     * @return ロールレスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<RoleResponse> getById(@PathVariable Long id) {
        return Result.ok(RoleAssembler.toResponse(service.getById(id)));
    }

    /**
     * 指定ロールの認可設定を取得する。
     *
     * @param id 対象のロールID
     * @return ロール認可設定レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}/authorization")
    public Result<RoleAuthorizationResponse> getAuthorization(@PathVariable Long id) {
        return Result.ok(authorizationService.getAuthorization(id));
    }

    /**
     * ロールを新規作成する。
     *
     * @param request 処理対象のロール作成リクエスト
     * @return ロールレスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<RoleResponse> create(@RequestBody @Valid RoleCreateRequest request) {
        return Result.ok(RoleAssembler.toResponse(service.create(RoleAssembler.toCommand(request))));
    }

    /**
     * ロールを更新する。
     *
     * @param id 対象のロールID
     * @param request 処理対象のロール更新リクエスト
     * @return ロールレスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<RoleResponse> update(@PathVariable Long id, @RequestBody @Valid RoleUpdateRequest request) {
        return Result.ok(RoleAssembler.toResponse(service.update(id, RoleAssembler.toCommand(request))));
    }

    /**
     * ロールを割り当てる。
     *
     * @param id 対象のロールID
     * @param request 処理対象のロール割当リクエスト
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/menus")
    @Deprecated(since = "0.0.1", forRemoval = false)
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody @Valid RoleAssignRequest request) {
        authorizationService.assignMenus(RoleAuthorizationAssembler.toMenuAssignCommand(id, request));
        return Result.ok();
    }

    /**
     * ロールを割り当てる。
     *
     * @param id 対象のロールID
     * @param request 処理対象のロール割当リクエスト
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/permissions")
    @Deprecated(since = "0.0.1", forRemoval = false)
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody @Valid RoleAssignRequest request) {
        authorizationService.assignPermissions(RoleAuthorizationAssembler.toPermissionAssignCommand(id, request));
        return Result.ok();
    }

    /**
     * ロールを保存する。
     *
     * @param id 対象のロールID
     * @param request 処理対象のロール認可設定保存リクエスト
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/authorization")
    public Result<Void> saveAuthorization(@PathVariable Long id, @RequestBody @Valid RoleAuthorizationSaveRequest request) {
        authorizationService.saveAuthorization(RoleAuthorizationAssembler.toSaveCommand(id, request));
        return Result.ok();
    }

    /**
     * ロールを有効化する。
     *
     * @param id 対象のロールID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * ロールを無効化する。
     *
     * @param id 対象のロールID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDのロールを削除する。
     *
     * @param id 対象のロールID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
