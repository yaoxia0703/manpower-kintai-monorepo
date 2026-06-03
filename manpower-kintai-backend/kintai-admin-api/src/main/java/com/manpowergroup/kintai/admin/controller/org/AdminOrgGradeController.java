package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.org.GradeAssembler;
import com.manpowergroup.kintai.system.application.dto.org.GradeResponse;
import com.manpowergroup.kintai.system.application.dto.org.request.GradeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.org.request.GradeUpdateRequest;
import com.manpowergroup.kintai.system.application.service.org.OrgGradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/org/grades")
@RequiredArgsConstructor
public class AdminOrgGradeController {

    private final OrgGradeService service;

    @GetMapping
    public Result<PageResult<GradeResponse>> page(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.pageByCompany(companyId, PageRequest.of(page, size)).map(GradeAssembler::toResponse));
    }

    @GetMapping("/list")
    public Result<List<GradeResponse>> list(@RequestParam Long companyId) {
        return Result.ok(service.listByCompany(companyId).stream().map(GradeAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<GradeResponse> getById(@PathVariable Long id) {
        return Result.ok(GradeAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<GradeResponse> create(@RequestBody @Valid GradeCreateRequest request) {
        return Result.ok(GradeAssembler.toResponse(service.create(GradeAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<GradeResponse> update(@PathVariable Long id, @RequestBody @Valid GradeUpdateRequest request) {
        return Result.ok(GradeAssembler.toResponse(service.update(id, GradeAssembler.toCommand(request))));
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
