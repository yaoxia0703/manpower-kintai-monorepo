package com.manpowergroup.kintai.admin.controller.hr;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingOptionsResponse;
import com.manpowergroup.kintai.employee.application.dto.hr.request.EmployeeOnboardingRequest;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingResponse;
import com.manpowergroup.kintai.employee.application.service.hr.EmployeeOnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/hr/onboarding")
@RequiredArgsConstructor
public class AdminHrOnboardingController {

    private final EmployeeOnboardingService onboardingService;

    @GetMapping("/options")
    public Result<EmployeeOnboardingOptionsResponse> options(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestParam(required = false) Long companyId) {
        return Result.ok(onboardingService.options(principal.employeeId(), companyId));
    }

    @PostMapping("/employees")
    public Result<EmployeeOnboardingResponse> onboardEmployee(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestBody @Valid EmployeeOnboardingRequest request) {
        return Result.ok(onboardingService.onboard(request, principal.employeeId()));
    }
}
