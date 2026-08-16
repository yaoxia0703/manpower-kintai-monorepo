package com.manpowergroup.kintai.hr.controller.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateFilterOptionsResponse;
import com.manpowergroup.kintai.employee.application.service.manager.ManagerSubordinateService;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.hr.application.service.emp.EmployeeService;
import com.manpowergroup.kintai.hr.assembler.emp.EmployeeDirectoryAssembler;
import com.manpowergroup.kintai.hr.assembler.emp.EmployeeRegisterAssembler;
import com.manpowergroup.kintai.hr.controller.emp.request.EmployeeDirectoryRequest;
import com.manpowergroup.kintai.hr.controller.emp.request.EmployeeRegisterRequest;
import com.manpowergroup.kintai.hr.controller.emp.response.EmployeeDirectoryResponse;
import com.manpowergroup.kintai.hr.controller.emp.response.EmployeeRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hr/emp/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ManagerSubordinateService subordinateService;

    /**
     * 指定された検索条件に基づいて社員一覧をページング取得する。
     *
     * @param request 社員一覧の検索条件およびページング条件
     * @return ページングされた社員一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<JoinPageResult<EmployeeDirectoryResponse>> pageDirectory(
        @Valid @ModelAttribute EmployeeDirectoryRequest request) {

        var query = EmployeeDirectoryAssembler.toQuery(request);
        var result = employeeService.pageDirectory(
            query,
            request.page(),
            request.size()
        );

        return Result.ok(EmployeeDirectoryAssembler.toResponse(result));
    }


    @GetMapping("/options")
    public Result<SubordinateFilterOptionsResponse> options() {
        return Result.ok(subordinateService.options(null));
    }

    @PostMapping("/register")
    public Result<EmployeeRegisterResponse> registerEmployee(
        @AuthenticationPrincipal LoginPrincipal principal,
        @RequestBody @Valid EmployeeRegisterRequest request) {

        var entry = EmployeeRegisterAssembler.toEntry(request);
        return Result.ok(EmployeeRegisterAssembler.toResponse(employeeService.registerEmployee(entry, principal.employeeId())));
    }
}
