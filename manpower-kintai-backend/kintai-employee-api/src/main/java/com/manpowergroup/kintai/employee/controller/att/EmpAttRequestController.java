package com.manpowergroup.kintai.employee.controller.att;

import com.manpowergroup.kintai.attendance.application.command.att.AttRequestCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.att.AttRequestUpdateCommand;
import com.manpowergroup.kintai.attendance.application.dto.att.request.AttRequestCreateRequest;
import com.manpowergroup.kintai.attendance.application.dto.att.request.AttRequestUpdateRequest;
import com.manpowergroup.kintai.attendance.application.dto.att.response.AttRequestResponse;
import com.manpowergroup.kintai.attendance.application.service.att.AttRequestService;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee/att/requests")
@RequiredArgsConstructor
public class EmpAttRequestController {

    private final AttRequestService requestService;
    private final EmpEmployeeService employeeService;

    /**
     * 指定された条件に一致する勤怠申請を一覧取得する。
     *
     * @param principal ログインユーザー情報
     * @return 勤怠申請レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<AttRequestResponse>> list(
            @AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(requestService.listByEmployee(principal.employeeId())
                .stream()
                .map(AttRequestResponse::from)
                .toList());
    }

    /**
     * 勤怠申請を新規作成する。
     *
     * @param principal ログインユーザー情報
     * @param request 処理対象の勤怠申請作成リクエスト
     * @return 勤怠申請レスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<AttRequestResponse> create(
            @AuthenticationPrincipal LoginPrincipal principal,
            @RequestBody @Valid AttRequestCreateRequest request) {
        EmpEmployee employee = employeeService.getById(principal.employeeId());
        return Result.ok(AttRequestResponse.from(requestService.create(
                new AttRequestCreateCommand(
                        employee.getId(),
                        employee.getCompanyId(),
                        request.requestType(),
                        request.startDate(),
                        request.endDate(),
                        request.startTime(),
                        request.endTime(),
                        request.days(),
                        request.minutes(),
                        request.reason()))));
    }

    /**
     * 勤怠申請を更新する。
     *
     * @param principal ログインユーザー情報
     * @param requestId 対象の申請ID
     * @param request 処理対象の勤怠申請更新リクエスト
     * @return 勤怠申請レスポンスを含むAPIレスポンス
     */
    @PutMapping("/{requestId}")
    public Result<AttRequestResponse> update(
            @AuthenticationPrincipal LoginPrincipal principal,
            @PathVariable Long requestId,
            @RequestBody @Valid AttRequestUpdateRequest request) {
        return Result.ok(AttRequestResponse.from(requestService.update(
                new AttRequestUpdateCommand(
                        requestId,
                        principal.employeeId(),
                        request.requestType(),
                        request.startDate(),
                        request.endDate(),
                        request.startTime(),
                        request.endTime(),
                        request.days(),
                        request.minutes(),
                        request.reason()))));
    }

    /**
     * 勤怠申請を取り消す。
     *
     * @param principal ログインユーザー情報
     * @param requestId 対象の申請ID
     * @return 処理結果を示すAPIレスポンス
     */
    @PostMapping("/{requestId}/cancel")
    public Result<Void> cancel(
            @AuthenticationPrincipal LoginPrincipal principal,
            @PathVariable Long requestId) {
        requestService.cancel(principal.employeeId(), requestId);
        return Result.ok();
    }
}
