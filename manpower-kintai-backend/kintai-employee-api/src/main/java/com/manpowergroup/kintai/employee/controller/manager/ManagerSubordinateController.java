package com.manpowergroup.kintai.employee.controller.manager;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.common.security.SecurityPermissions;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.system.application.service.manager.ManagerSubordinateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manager/emp/subordinates")
@RequiredArgsConstructor
public class ManagerSubordinateController {

    private final ManagerSubordinateService subordinateService;

    @GetMapping
    @PreAuthorize(SecurityPermissions.HAS_MANAGER_SUBORDINATE_READ)
    public Result<List<SubordinateEmployeeResponse>> listSubordinates(
            @AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(subordinateService.listSubordinates(principal.employeeId()));
    }
}
