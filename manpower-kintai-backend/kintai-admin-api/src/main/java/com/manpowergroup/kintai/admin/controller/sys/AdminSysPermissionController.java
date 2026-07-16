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

    @GetMapping
    public Result<PageResult<PermissionResponse>> page(
            @RequestParam(required = false) Long menuId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(menuId, keyword, PageRequest.of(page, size))
                .map(PermissionAssembler::toResponse));
    }

    @GetMapping("/{id}")
    public Result<PermissionResponse> getById(@PathVariable Long id) {
        return Result.ok(PermissionAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<PermissionResponse> create(@RequestBody @Valid PermissionCreateRequest request) {
        return Result.ok(PermissionAssembler.toResponse(service.create(PermissionAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<PermissionResponse> update(@PathVariable Long id, @RequestBody @Valid PermissionUpdateRequest request) {
        return Result.ok(PermissionAssembler.toResponse(service.update(id, PermissionAssembler.toCommand(request))));
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
