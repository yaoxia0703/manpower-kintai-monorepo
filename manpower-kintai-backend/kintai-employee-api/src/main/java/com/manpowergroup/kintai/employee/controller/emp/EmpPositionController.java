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

    /**
     * ログイン中の社員に設定された職位を取得する。
     *
     * @param principal ログインユーザー情報
     * @return 社員職位一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<EmpEmployeePosition>> getMyPositions(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(service.listActiveByEmployee(principal.employeeId()));
    }

    /**
     * ログイン中の社員の主職位を取得する。
     *
     * @param principal ログインユーザー情報
     * @return 社員職位を含むAPIレスポンス
     */
    @GetMapping("/primary")
    public Result<EmpEmployeePosition> getMyPrimaryPosition(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(service.getPrimaryByEmployee(principal.employeeId()));
    }
}


