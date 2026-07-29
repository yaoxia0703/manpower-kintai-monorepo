package com.manpowergroup.kintai.employee.controller.emp;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.application.command.emp.EmployeeUpdateCommand;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/emp/profile")
@RequiredArgsConstructor
public class EmpEmployeeController {

    private final EmpEmployeeService service;

    /**
     * ログイン中の社員プロフィールを取得する。
     *
     * @param principal ログインユーザー情報
     * @return 社員を含むAPIレスポンス
     */
    @GetMapping
    public Result<EmpEmployee> getMyProfile(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(service.getById(principal.employeeId()));
    }

    /**
     * ログイン中の社員プロフィールを更新する。
     *
     * @param principal ログインユーザー情報
     * @param employee 処理対象の社員
     * @return 社員を含むAPIレスポンス
     */
    @PutMapping
    public Result<EmpEmployee> updateMyProfile(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestBody EmpEmployee employee) {
        return Result.ok(service.update(principal.employeeId(), toCommand(employee)));
    }

    /**
     * 社員情報を社員更新コマンドへ変換する。
     *
     * @param employee 処理対象の社員
     * @return 社員更新コマンド
     */
    private EmployeeUpdateCommand toCommand(EmpEmployee employee) {
        return new EmployeeUpdateCommand(
                employee.getCompanyId(),
                employee.getEmployeeCode(),
                employee.getLastName(),
                employee.getFirstName(),
                employee.getLastNameKana(),
                employee.getFirstNameKana(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getGender(),
                employee.getBirthDate(),
                employee.getHireDate(),
                employee.getLeaveDate(),
                employee.getStatus()
        );
    }
}
