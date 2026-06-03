package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.I18nAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.I18nResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.I18nUpsertRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysI18nService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/i18n")
@RequiredArgsConstructor
public class AdminSysI18nController {

    private final SysI18nService service;

    @GetMapping
    public Result<List<I18nResponse>> listByRef(
            @RequestParam String refType,
            @RequestParam Long refId) {
        return Result.ok(service.listByRef(refType, refId).stream().map(I18nAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<I18nResponse> getById(@PathVariable Long id) {
        return Result.ok(I18nAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<I18nResponse> upsert(@RequestBody @Valid I18nUpsertRequest request) {
        return Result.ok(I18nAssembler.toResponse(service.upsert(I18nAssembler.toCommand(request))));
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }

    @DeleteMapping("/by-ref")
    public Result<Void> removeByRef(
            @RequestParam String refType,
            @RequestParam Long refId) {
        service.removeByRef(refType, refId);
        return Result.ok();
    }
}
