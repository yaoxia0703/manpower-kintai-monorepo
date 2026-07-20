package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.assembler.org.CompanyAssembler;
import com.manpowergroup.kintai.employee.application.dto.org.response.CompanyResponse;
import com.manpowergroup.kintai.employee.application.dto.org.request.CompanyCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.org.request.CompanyUpdateRequest;
import com.manpowergroup.kintai.employee.application.service.org.OrgCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/org/companies")
@RequiredArgsConstructor
public class AdminOrgCompanyController {

    private final OrgCompanyService service;

    @GetMapping
    public Result<PageResult<CompanyResponse>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.page(PageRequest.of(page, size)).map(CompanyAssembler::toResponse));
    }

    @GetMapping("/enabled")
    public Result<List<CompanyResponse>> listEnabled() {
        return Result.ok(service.listEnabled().stream().map(CompanyAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<CompanyResponse> getById(@PathVariable Long id) {
        return Result.ok(CompanyAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<CompanyResponse> create(@RequestBody @Valid CompanyCreateRequest request) {
        return Result.ok(CompanyAssembler.toResponse(service.create(CompanyAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<CompanyResponse> update(@PathVariable Long id, @RequestBody @Valid CompanyUpdateRequest request) {
        return Result.ok(CompanyAssembler.toResponse(service.update(id, CompanyAssembler.toCommand(request))));
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
