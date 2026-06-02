package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.common.security.SecurityPermissions;
import com.manpowergroup.kintai.system.application.dto.sys.RoleAuthorizationRequest;
import com.manpowergroup.kintai.system.application.dto.sys.RoleAuthorizationResponse;
import com.manpowergroup.kintai.system.application.dto.sys.RoleRequest;
import com.manpowergroup.kintai.system.application.dto.sys.RoleResponse;
import com.manpowergroup.kintai.system.application.service.sys.SysRoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ロールマスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/sys/roles")
@RequiredArgsConstructor
public class AdminSysRoleController {

    private final SysRoleService service;

    // ロール一覧をページング取得
    @GetMapping
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_READ)
    public Result<PageResult<RoleResponse>> page(
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(companyId, PageRequest.of(page, size)).map(RoleResponse::from));
    }

    // 会社IDでロール一覧を全取得（ドロップダウン用）
    @GetMapping("/list")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_READ)
    public Result<List<RoleResponse>> list(@RequestParam(required = false) Long companyId) {
        return Result.ok(service.listByCompany(companyId).stream().map(RoleResponse::from).toList());
    }

    // IDでロールを取得
    @GetMapping("/{id}")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_READ)
    public Result<RoleResponse> getById(@PathVariable Long id) {
        return Result.ok(RoleResponse.from(service.getById(id)));
    }

    @GetMapping("/{id}/authorization")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_READ)
    public Result<RoleAuthorizationResponse> getAuthorization(@PathVariable Long id) {
        return Result.ok(service.getAuthorization(id));
    }

    // ロールを新規作成
    @PostMapping
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
    public Result<RoleResponse> create(@RequestBody @Valid RoleRequest request) {
        return Result.ok(RoleResponse.from(service.create(request.toEntity())));
    }

    // ロールを更新
    @PutMapping("/{id}")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
    public Result<RoleResponse> update(@PathVariable Long id, @RequestBody @Valid RoleRequest request) {
        return Result.ok(RoleResponse.from(service.update(id, request.toEntity())));
    }

    // ロールにメニューを割り当て
    @PutMapping("/{id}/menus")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody @Valid AssignRequest request) {
        service.assignMenus(id, request.getIds());
        return Result.ok();
    }

    // ロールに権限を割り当て
    @PutMapping("/{id}/permissions")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody @Valid AssignRequest request) {
        service.assignPermissions(id, request.getIds());
        return Result.ok();
    }

    @PutMapping("/{id}/authorization")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
    public Result<Void> saveAuthorization(@PathVariable Long id, @RequestBody @Valid RoleAuthorizationRequest request) {
        service.saveAuthorization(id, request);
        return Result.ok();
    }

    // ロールを有効化
    @PutMapping("/{id}/enable")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // ロールを無効化
    @PutMapping("/{id}/disable")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // ロールを削除（論理削除）
    @DeleteMapping("/{id}")
    @PreAuthorize(SecurityPermissions.HAS_ADMIN_ROLE_WRITE)
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }

    @Data
    static class AssignRequest {
        @NotEmpty
        private List<Long> ids;
    }
}


