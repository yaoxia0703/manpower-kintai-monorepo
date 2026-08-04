package com.manpowergroup.kintai.hr.controller.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.hr.application.service.emp.EmployeeDirectoryService;
import com.manpowergroup.kintai.hr.assembler.emp.EmployeeDirectoryAssembler;
import com.manpowergroup.kintai.hr.controller.emp.request.EmployeeDirectoryRequest;
import com.manpowergroup.kintai.hr.controller.emp.response.EmployeeDirectoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/hr/employee")
@RequiredArgsConstructor
public class EmployeeDirectoryController {

    private final EmployeeDirectoryService employeeDirectoryService;

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
        var result = employeeDirectoryService.pageDirectory(
            query,
            request.page(),
            request.size()
        );

        return Result.ok(EmployeeDirectoryAssembler.toResponse(result));
    }

}
