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

    @GetMapping
    public Result<PageResult<RoleResponse>> page(
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(companyId, PageRequest.of(page, size)).map(RoleAssembler::toResponse));
    }

    @GetMapping("/list")
    public Result<List<RoleResponse>> list(@RequestParam(required = false) Long companyId) {
        return Result.ok(service.listByCompany(companyId).stream().map(RoleAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<RoleResponse> getById(@PathVariable Long id) {
        return Result.ok(RoleAssembler.toResponse(service.getById(id)));
    }

    @GetMapping("/{id}/authorization")
    public Result<RoleAuthorizationResponse> getAuthorization(@PathVariable Long id) {
        return Result.ok(authorizationService.getAuthorization(id));
    }

    @PostMapping
    public Result<RoleResponse> create(@RequestBody @Valid RoleCreateRequest request) {
        return Result.ok(RoleAssembler.toResponse(service.create(RoleAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<RoleResponse> update(@PathVariable Long id, @RequestBody @Valid RoleUpdateRequest request) {
        return Result.ok(RoleAssembler.toResponse(service.update(id, RoleAssembler.toCommand(request))));
    }

    @PutMapping("/{id}/menus")
    @Deprecated(since = "0.0.1", forRemoval = false)
    public Result<Void> assignMenus(@PathVariable Long id, @RequestBody @Valid RoleAssignRequest request) {
        authorizationService.assignMenus(RoleAuthorizationAssembler.toMenuAssignCommand(id, request));
        return Result.ok();
    }

    @PutMapping("/{id}/permissions")
    @Deprecated(since = "0.0.1", forRemoval = false)
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody @Valid RoleAssignRequest request) {
        authorizationService.assignPermissions(RoleAuthorizationAssembler.toPermissionAssignCommand(id, request));
        return Result.ok();
    }

    @PutMapping("/{id}/authorization")
    public Result<Void> saveAuthorization(@PathVariable Long id, @RequestBody @Valid RoleAuthorizationSaveRequest request) {
        authorizationService.saveAuthorization(RoleAuthorizationAssembler.toSaveCommand(id, request));
        return Result.ok();
    }

    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
