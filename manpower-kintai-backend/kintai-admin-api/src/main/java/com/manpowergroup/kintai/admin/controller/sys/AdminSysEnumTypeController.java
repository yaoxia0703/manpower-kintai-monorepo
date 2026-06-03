package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.EnumTypeAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.EnumTypeResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumTypeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumTypeUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/enum-types")
@RequiredArgsConstructor
public class AdminSysEnumTypeController {

    private final SysEnumTypeService service;

    @GetMapping
    public Result<PageResult<EnumTypeResponse>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(PageRequest.of(page, size)).map(EnumTypeAssembler::toResponse));
    }

    @GetMapping("/enabled")
    public Result<List<EnumTypeResponse>> listEnabled() {
        return Result.ok(service.listEnabled().stream().map(EnumTypeAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<EnumTypeResponse> getById(@PathVariable Long id) {
        return Result.ok(EnumTypeAssembler.toResponse(service.getById(id)));
    }

    @GetMapping("/code/{code}")
    public Result<EnumTypeResponse> getByCode(@PathVariable String code) {
        return Result.ok(EnumTypeAssembler.toResponse(service.getByCode(code)));
    }

    @PostMapping
    public Result<EnumTypeResponse> create(@RequestBody @Valid EnumTypeCreateRequest request) {
        return Result.ok(EnumTypeAssembler.toResponse(service.create(EnumTypeAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<EnumTypeResponse> update(@PathVariable Long id, @RequestBody @Valid EnumTypeUpdateRequest request) {
        return Result.ok(EnumTypeAssembler.toResponse(service.update(id, EnumTypeAssembler.toCommand(request))));
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
