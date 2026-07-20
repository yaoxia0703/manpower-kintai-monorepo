package com.manpowergroup.kintai.admin.controller.emp;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.assembler.emp.EmployeeAssembler;
import com.manpowergroup.kintai.employee.application.dto.emp.response.EmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeeCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeeUpdateRequest;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 社員マスタ管理Controller（管理者用）
@RestController
@RequestMapping("/admin/emp/employees")
@RequiredArgsConstructor
public class AdminEmpEmployeeController {

    private final EmpEmployeeService service;

    // 会社IDで社員一覧をページング取得
    @GetMapping
    public Result<PageResult<EmployeeResponse>> page(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.pageByCompany(companyId, PageRequest.of(page, size)).map(EmployeeAssembler::toResponse));
    }

    // 氏名キーワードで社員を検索
    @GetMapping("/search")
    public Result<PageResult<EmployeeResponse>> search(
            @RequestParam Long companyId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.ok(service.searchByName(companyId, keyword, PageRequest.of(page, size)).map(EmployeeAssembler::toResponse));
    }

    // IDで社員を取得
    @GetMapping("/{id}")
    public Result<EmployeeResponse> getById(@PathVariable Long id) {
        return Result.ok(EmployeeAssembler.toResponse(service.getById(id)));
    }

    // 社員を新規作成
    @PostMapping
    public Result<EmployeeResponse> create(@RequestBody @Valid EmployeeCreateRequest request) {
        return Result.ok(EmployeeAssembler.toResponse(service.create(EmployeeAssembler.toCommand(request))));
    }

    // 社員情報を更新
    @PutMapping("/{id}")
    public Result<EmployeeResponse> update(@PathVariable Long id, @RequestBody @Valid EmployeeUpdateRequest request) {
        return Result.ok(EmployeeAssembler.toResponse(service.update(id, EmployeeAssembler.toCommand(request))));
    }

    // 社員を在職状態に変更
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    // 社員を退職状態に変更
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    // 社員を削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


