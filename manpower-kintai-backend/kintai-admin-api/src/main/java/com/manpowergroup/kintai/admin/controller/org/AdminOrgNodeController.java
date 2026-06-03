package com.manpowergroup.kintai.admin.controller.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.org.NodeAssembler;
import com.manpowergroup.kintai.system.application.dto.org.NodeResponse;
import com.manpowergroup.kintai.system.application.dto.org.request.NodeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.org.request.NodeUpdateRequest;
import com.manpowergroup.kintai.system.application.service.org.OrgNodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/org/nodes")
@RequiredArgsConstructor
public class AdminOrgNodeController {

    private final OrgNodeService service;

    @GetMapping
    public Result<PageResult<NodeResponse>> page(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.pageByCompany(companyId, PageRequest.of(page, size)).map(NodeAssembler::toResponse));
    }

    @GetMapping("/tree")
    public Result<List<NodeResponse>> listTree(@RequestParam Long companyId) {
        return Result.ok(service.listEnabledByCompany(companyId).stream().map(NodeAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<NodeResponse> getById(@PathVariable Long id) {
        return Result.ok(NodeAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<NodeResponse> create(@RequestBody @Valid NodeCreateRequest request) {
        return Result.ok(NodeAssembler.toResponse(service.create(NodeAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<NodeResponse> update(@PathVariable Long id, @RequestBody @Valid NodeUpdateRequest request) {
        return Result.ok(NodeAssembler.toResponse(service.update(id, NodeAssembler.toCommand(request))));
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
