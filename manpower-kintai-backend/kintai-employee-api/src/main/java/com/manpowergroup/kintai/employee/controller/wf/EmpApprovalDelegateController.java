package com.manpowergroup.kintai.employee.controller.wf;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.adapter.wf.ApprovalManagerEligibility;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/approval-delegates")
@RequiredArgsConstructor
public class EmpApprovalDelegateController {

    private final EmpEmployeeService employeeService;
    private final ApprovalManagerEligibility approvalManagerEligibility;

    @GetMapping
    public Result<PageResult<ApprovalDelegateCandidateResponse>> search(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        EmpEmployee current = employeeService.getById(principal.employeeId());
        PageResult<ApprovalDelegateCandidateResponse> candidates = employeeService
                .searchByName(current.getCompanyId(), keyword, PageRequest.of(page, size))
                .map(this::toResponse);
        candidates.setRecords(candidates.getRecords().stream()
                .filter(candidate -> approvalManagerEligibility.canApprove(candidate.employeeId()))
                .toList());
        return Result.ok(candidates);
    }

    private ApprovalDelegateCandidateResponse toResponse(EmpEmployee employee) {
        return new ApprovalDelegateCandidateResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getLastName() + " " + employee.getFirstName());
    }
}
