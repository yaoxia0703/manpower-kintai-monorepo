package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.EnumValueAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.EnumValueResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumValueCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumValueUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/enum-values")
@RequiredArgsConstructor
public class AdminSysEnumValueController {

    private final SysEnumValueService service;

    @GetMapping
    public Result<List<EnumValueResponse>> listByEnumType(@RequestParam String enumTypeCode) {
        return Result.ok(service.listByEnumTypeCode(enumTypeCode).stream().map(EnumValueAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<EnumValueResponse> getById(@PathVariable Long id) {
        return Result.ok(EnumValueAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<EnumValueResponse> create(@RequestBody @Valid EnumValueCreateRequest request) {
        return Result.ok(EnumValueAssembler.toResponse(service.create(EnumValueAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<EnumValueResponse> update(@PathVariable Long id, @RequestBody @Valid EnumValueUpdateRequest request) {
        return Result.ok(EnumValueAssembler.toResponse(service.update(id, EnumValueAssembler.toCommand(request))));
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
