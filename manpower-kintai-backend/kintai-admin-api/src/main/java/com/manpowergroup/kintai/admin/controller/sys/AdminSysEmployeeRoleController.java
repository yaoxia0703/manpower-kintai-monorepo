package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.EmployeeRoleAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.response.EmployeeRoleResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EmployeeRoleAssignRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EmployeeRoleUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysEmployeeRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/employee-roles")
@RequiredArgsConstructor
public class AdminSysEmployeeRoleController {

    private final SysEmployeeRoleService service;

    @GetMapping
    public Result<List<EmployeeRoleResponse>> listActive(@RequestParam Long employeeId) {
        return Result.ok(service.listActiveByEmployee(employeeId).stream().map(EmployeeRoleAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<EmployeeRoleResponse> getById(@PathVariable Long id) {
        return Result.ok(EmployeeRoleAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<EmployeeRoleResponse> assign(@RequestBody @Valid EmployeeRoleAssignRequest request) {
        return Result.ok(EmployeeRoleAssembler.toResponse(service.assign(EmployeeRoleAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<EmployeeRoleResponse> update(@PathVariable Long id, @RequestBody @Valid EmployeeRoleUpdateRequest request) {
        return Result.ok(EmployeeRoleAssembler.toResponse(service.update(id, EmployeeRoleAssembler.toCommand(request))));
    }

    @DeleteMapping("/{id}")
    public Result<Void> revoke(@PathVariable Long id) {
        service.revoke(id);
        return Result.ok();
    }
}
