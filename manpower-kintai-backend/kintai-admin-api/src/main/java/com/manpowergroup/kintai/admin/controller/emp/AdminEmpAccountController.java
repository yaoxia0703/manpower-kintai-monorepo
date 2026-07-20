package com.manpowergroup.kintai.admin.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.assembler.emp.AccountAssembler;
import com.manpowergroup.kintai.employee.application.dto.emp.response.AccountResponse;
import com.manpowergroup.kintai.employee.application.dto.emp.request.AccountCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.emp.request.AccountUpdateRequest;
import com.manpowergroup.kintai.employee.application.service.emp.EmpAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/emp/accounts")
@RequiredArgsConstructor
public class AdminEmpAccountController {

    private final EmpAccountService service;

    @GetMapping("/by-employee/{employeeId}")
    public Result<AccountResponse> getByEmployeeId(@PathVariable Long employeeId) {
        return Result.ok(AccountAssembler.toResponse(service.getByEmployeeId(employeeId)));
    }

    @GetMapping("/{id}")
    public Result<AccountResponse> getById(@PathVariable Long id) {
        return Result.ok(AccountAssembler.toResponse(service.getById(id)));
    }

    @PostMapping
    public Result<AccountResponse> create(@RequestBody @Valid AccountCreateRequest request) {
        return Result.ok(AccountAssembler.toResponse(service.create(AccountAssembler.toCommand(request))));
    }

    @PutMapping("/{id}")
    public Result<AccountResponse> update(@PathVariable Long id, @RequestBody @Valid AccountUpdateRequest request) {
        return Result.ok(AccountAssembler.toResponse(service.update(id, AccountAssembler.toCommand(request))));
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
