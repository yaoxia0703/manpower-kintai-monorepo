package com.manpowergroup.kintai.admin.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.emp.EmployeePositionAssembler;
import com.manpowergroup.kintai.system.application.dto.emp.EmployeePositionResponse;
import com.manpowergroup.kintai.system.application.dto.emp.request.EmployeePositionCreateRequest;
import com.manpowergroup.kintai.system.application.dto.emp.request.EmployeePositionUpdateRequest;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeePositionService;
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
@RequestMapping("/admin/emp/positions")
@RequiredArgsConstructor
public class AdminEmpEmployeePositionController {

    private final EmpEmployeePositionService service;

    @GetMapping
    public Result<List<EmployeePositionResponse>> listActive(@RequestParam Long employeeId) {
        return Result.ok(service.listActiveByEmployee(employeeId).stream().map(EmployeePositionAssembler::toResponse).toList());
    }

    @GetMapping("/history")
    public Result<List<EmployeePositionResponse>> listAll(@RequestParam Long employeeId) {
        return Result.ok(service.listAllByEmployee(employeeId).stream().map(EmployeePositionAssembler::toResponse).toList());
    }

    @GetMapping("/{id}")
    public Result<EmployeePositionResponse> getById(@PathVariable Long id) {
        return Result.ok(EmployeePositionAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<EmployeePositionResponse> create(@RequestBody @Valid EmployeePositionCreateRequest request) {
        return Result.ok(EmployeePositionAssembler.toResponse(service.create(EmployeePositionAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<EmployeePositionResponse> update(@PathVariable Long id, @RequestBody @Valid EmployeePositionUpdateRequest request) {
        return Result.ok(EmployeePositionAssembler.toResponse(service.update(id, EmployeePositionAssembler.toCommand(request))));
    }

    @PutMapping("/{id}/terminate")
    public Result<Void> terminate(@PathVariable Long id) {
        service.terminate(id);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
