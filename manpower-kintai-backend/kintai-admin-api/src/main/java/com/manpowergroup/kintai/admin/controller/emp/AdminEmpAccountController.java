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

    /**
     * 社員IDに対応する社員アカウントを取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 社員アカウントレスポンスを含むAPIレスポンス
     */
    @GetMapping("/by-employee/{employeeId}")
    public Result<AccountResponse> getByEmployeeId(@PathVariable Long employeeId) {
        return Result.ok(AccountAssembler.toResponse(service.getByEmployeeId(employeeId)));
    }

    /**
     * IDに対応する社員アカウントを取得する。
     *
     * @param id 対象の社員アカウントID
     * @return 社員アカウントレスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<AccountResponse> getById(@PathVariable Long id) {
        return Result.ok(AccountAssembler.toResponse(service.getById(id)));
    }

    /**
     * 社員アカウントを新規作成する。
     *
     * @param request 処理対象の社員アカウント作成リクエスト
     * @return 社員アカウントレスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<AccountResponse> create(@RequestBody @Valid AccountCreateRequest request) {
        return Result.ok(AccountAssembler.toResponse(service.create(AccountAssembler.toCommand(request))));
    }

    /**
     * 社員アカウントを更新する。
     *
     * @param id 対象の社員アカウントID
     * @param request 処理対象の社員アカウント更新リクエスト
     * @return 社員アカウントレスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<AccountResponse> update(@PathVariable Long id, @RequestBody @Valid AccountUpdateRequest request) {
        return Result.ok(AccountAssembler.toResponse(service.update(id, AccountAssembler.toCommand(request))));
    }

    /**
     * 社員アカウントを有効化する。
     *
     * @param id 対象の社員アカウントID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * 社員アカウントを無効化する。
     *
     * @param id 対象の社員アカウントID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDの社員アカウントを削除する。
     *
     * @param id 対象の社員アカウントID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
