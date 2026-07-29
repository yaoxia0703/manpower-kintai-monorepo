package com.manpowergroup.kintai.employee.controller.att;

import com.manpowergroup.kintai.attendance.application.assembler.timesheet.TimesheetAssembler;
import com.manpowergroup.kintai.attendance.application.dto.timesheet.request.TimesheetSaveRequest;
import com.manpowergroup.kintai.attendance.application.dto.timesheet.response.TimesheetMonthResponse;
import com.manpowergroup.kintai.attendance.application.service.att.AttTimesheetQueryService;
import com.manpowergroup.kintai.attendance.application.service.att.AttTimesheetRecordService;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee/att/timesheet")
@RequiredArgsConstructor
public class EmpTimesheetController {

    private final AttTimesheetQueryService timesheetQueryService;
    private final AttTimesheetRecordService timesheetRecordService;
    private final EmpEmployeeService employeeService;

    /**
     * 指定社員・対象年月の勤怠表を取得する。
     *
     * @param principal ログインユーザー情報
     * @param year 対象年
     * @param month 対象月
     * @return 月次勤怠表レスポンスを含むAPIレスポンス
     */
    @GetMapping
    public Result<TimesheetMonthResponse> getMonthlyTimesheet(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestParam int year,
            @RequestParam int month) {
        EmpEmployee employee = employeeService.getById(principal.employeeId());
        return Result.ok(timesheetQueryService.getMonthlyTimesheet(
                TimesheetAssembler.toMonthQuery(employee.getId(), employee.getCompanyId(), year, month)));
    }

    /**
     * 指定日の勤怠記録を保存する。
     *
     * @param principal ログインユーザー情報
     * @param request 処理対象の勤怠表保存リクエスト
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping
    public Result<Void> saveRecord(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestBody @Valid TimesheetSaveRequest request) {
        EmpEmployee employee = employeeService.getById(principal.employeeId());
        timesheetRecordService.saveRecord(
                TimesheetAssembler.toSaveCommand(employee.getId(), employee.getCompanyId(), request));
        return Result.ok();
    }

    /**
     * 指定日の勤怠記録を削除する。
     *
     * @param principal ログインユーザー情報
     * @param recordId 対象の勤怠記録ID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{recordId}")
    public Result<Void> deleteRecord(
            @AuthenticationPrincipal LoginPrincipal principal,
            @PathVariable Long recordId) {
        timesheetRecordService.deleteRecord(TimesheetAssembler.toDeleteCommand(principal.employeeId(), recordId));
        return Result.ok();
    }
}
