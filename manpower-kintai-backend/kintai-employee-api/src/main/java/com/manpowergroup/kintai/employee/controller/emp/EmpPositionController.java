package com.manpowergroup.kintai.employee.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeePositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 社員職位Controller（社員向け・自分自身のみ）
@RestController
@RequestMapping("/employee/emp/positions")
@RequiredArgsConstructor
public class EmpPositionController {

    private final EmpEmployeePositionService service;

    // ログイン中の社員自身の有効な職位一覧を取得
    @GetMapping
    public Result<List<EmpEmployeePosition>> getMyPositions(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(service.listActiveByEmployee(principal.employeeId()));
    }

    // ログイン中の社員の主務職位を取得
    @GetMapping("/primary")
    public Result<EmpEmployeePosition> getMyPrimaryPosition(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(service.getPrimaryByEmployee(principal.employeeId()));
    }
}


