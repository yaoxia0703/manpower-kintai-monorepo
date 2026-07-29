package com.manpowergroup.kintai.admin.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.assembler.emp.EmployeePositionAssembler;
import com.manpowergroup.kintai.employee.application.dto.emp.response.EmployeePositionResponse;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeePositionCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.emp.request.EmployeePositionUpdateRequest;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeePositionService;
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

    /**
     * 有効な社員職位を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員職位レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<EmployeePositionResponse>> listActive(@RequestParam Long employeeId) {
        return Result.ok(service.listActiveByEmployee(employeeId).stream().map(EmployeePositionAssembler::toResponse).toList());
    }

    /**
     * すべての社員職位を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員職位レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping("/history")
    public Result<List<EmployeePositionResponse>> listAll(@RequestParam Long employeeId) {
        return Result.ok(service.listAllByEmployee(employeeId).stream().map(EmployeePositionAssembler::toResponse).toList());
    }

    /**
     * IDに対応する社員職位を取得する。
     *
     * @param id 対象の社員職位ID
     * @return 社員職位レスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<EmployeePositionResponse> getById(@PathVariable Long id) {
        return Result.ok(EmployeePositionAssembler.toResponse(service.getById(id)));
    }

    /**
     * 社員職位を新規作成する。
     *
     * @param request 処理対象の社員職位作成リクエスト
     * @return 社員職位レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<EmployeePositionResponse> create(@RequestBody @Valid EmployeePositionCreateRequest request) {
        return Result.ok(EmployeePositionAssembler.toResponse(service.create(EmployeePositionAssembler.toCommand(request))));
    }

    /**
     * 社員職位を更新する。
     *
     * @param id 対象の社員職位ID
     * @param request 処理対象の社員職位更新リクエスト
     * @return 社員職位レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<EmployeePositionResponse> update(@PathVariable Long id, @RequestBody @Valid EmployeePositionUpdateRequest request) {
        return Result.ok(EmployeePositionAssembler.toResponse(service.update(id, EmployeePositionAssembler.toCommand(request))));
    }

    /**
     * 社員職位を終了状態に変更する。
     *
     * @param id 対象の社員職位ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/terminate")
    public Result<Void> terminate(@PathVariable Long id) {
        service.terminate(id);
        return Result.ok();
    }

    /**
     * 指定IDの社員職位を削除する。
     *
     * @param id 対象の社員職位ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
