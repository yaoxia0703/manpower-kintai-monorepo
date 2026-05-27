package com.manpowergroup.kintai.admin.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeePositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 社員職位管理Controller（管理者用）
@RestController
@RequestMapping("/admin/emp/positions")
@RequiredArgsConstructor
public class AdminEmpEmployeePositionController {

    private final EmpEmployeePositionService service;

    // 社員IDで有効な職位一覧を取得
    @GetMapping
    public Result<List<EmpEmployeePosition>> listActive(@RequestParam Long employeeId) {
        return Result.ok(service.listActiveByEmployee(employeeId));
    }

    // 社員IDで全職位を取得（履歴含む）
    @GetMapping("/history")
    public Result<List<EmpEmployeePosition>> listAll(@RequestParam Long employeeId) {
        return Result.ok(service.listAllByEmployee(employeeId));
    }

    // IDで社員職位を取得
    @GetMapping("/{id}")
    public Result<EmpEmployeePosition> getById(@PathVariable Long id) {
        return Result.ok(service.getById(id));
    }

    // 社員職位を新規追加
    @PostMapping
    public Result<EmpEmployeePosition> create(@RequestBody EmpEmployeePosition position) {
        return Result.ok(service.create(position));
    }

    // 社員職位を更新
    @PutMapping("/{id}")
    public Result<EmpEmployeePosition> update(@PathVariable Long id, @RequestBody EmpEmployeePosition position) {
        return Result.ok(service.update(id, position));
    }

    // 社員職位を終了（離任）
    @PutMapping("/{id}/terminate")
    public Result<Void> terminate(@PathVariable Long id) {
        service.terminate(id);
        return Result.ok();
    }

    // 社員職位を削除（論理削除）
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}


