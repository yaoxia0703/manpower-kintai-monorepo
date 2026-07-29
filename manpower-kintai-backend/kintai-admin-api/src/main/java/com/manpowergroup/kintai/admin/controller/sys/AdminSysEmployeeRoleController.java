package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.EmployeeRoleAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.response.EmployeeRoleResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EmployeeRoleAssignRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EmployeeRoleUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.EmployeeRoleAssignmentService;
import com.manpowergroup.kintai.system.application.service.sys.SysEmployeeRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/employee-roles")
@RequiredArgsConstructor
public class AdminSysEmployeeRoleController {

    private final SysEmployeeRoleService service;
    private final EmployeeRoleAssignmentService assignmentService;

    /**
     * 有効な社員ロール割当を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員ロール割当レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<EmployeeRoleResponse>> listActive(@RequestParam Long employeeId) {
        return Result.ok(service.listActiveByEmployee(employeeId).stream().map(EmployeeRoleAssembler::toResponse).toList());
    }

    /**
     * IDに対応する社員ロール割当を取得する。
     *
     * @param id 対象の社員ロール割当ID
     * @return 社員ロール割当レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<EmployeeRoleResponse> getById(@PathVariable Long id) {
        return Result.ok(EmployeeRoleAssembler.toResponse(service.getById(id)));
    }

    /**
     * 社員ロール割当を割り当てる。
     *
     * @param request 処理対象の社員ロール割当割当リクエスト
     * @return 社員ロール割当レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<EmployeeRoleResponse> assign(@RequestBody @Valid EmployeeRoleAssignRequest request) {
        return Result.ok(EmployeeRoleAssembler.toResponse(assignmentService.assign(EmployeeRoleAssembler.toCommand(request))));
    }

    /**
     * 社員ロール割当を更新する。
     *
     * @param id 対象の社員ロール割当ID
     * @param request 処理対象の社員ロール割当更新リクエスト
     * @return 社員ロール割当レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<EmployeeRoleResponse> update(@PathVariable Long id, @RequestBody @Valid EmployeeRoleUpdateRequest request) {
        return Result.ok(EmployeeRoleAssembler.toResponse(assignmentService.update(id, EmployeeRoleAssembler.toCommand(request))));
    }

    /**
     * 社員ロール割当の割当を解除する。
     *
     * @param id 対象の社員ロール割当ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> revoke(@PathVariable Long id) {
        assignmentService.revoke(id);
        return Result.ok();
    }
}
