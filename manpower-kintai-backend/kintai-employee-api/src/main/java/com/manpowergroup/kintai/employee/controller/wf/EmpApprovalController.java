package com.manpowergroup.kintai.employee.controller.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.request.ApprovalDecisionRequest;
import com.manpowergroup.kintai.attendance.application.dto.wf.request.ApprovalDelegateRequest;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailResponse;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;
import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalDecisionService;
import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalInboxService;
import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalHistoryService;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee/approvals")
@RequiredArgsConstructor
public class EmpApprovalController {

    private final ApprovalDecisionService service;
    private final ApprovalInboxService inboxService;
    private final ApprovalHistoryService historyService;

    @GetMapping("/pending")
    public Result<List<ApprovalInboxItem>> pending(
            @AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(inboxService.listPending(principal.employeeId()));
    }

    @GetMapping("/history")
    public Result<List<ApprovalHistoryItem>> history(
            @AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(historyService.listHistory(principal.employeeId()));
    }

    @GetMapping("/{approvalId}")
    public Result<ApprovalDetailResponse> detail(
            @AuthenticationPrincipal LoginPrincipal principal,
            @PathVariable Long approvalId) {
        return Result.ok(historyService.getDetail(principal.employeeId(), approvalId));
    }

    @PostMapping("/{approvalId}/approve")
    public Result<Void> approve(
            @AuthenticationPrincipal LoginPrincipal principal,
            @PathVariable Long approvalId,
            @RequestBody @Valid ApprovalDecisionRequest request) {
        service.approve(approvalId, principal.employeeId(), request.comment());
        return Result.ok();
    }

    @PostMapping("/{approvalId}/reject")
    public Result<Void> reject(
            @AuthenticationPrincipal LoginPrincipal principal,
            @PathVariable Long approvalId,
            @RequestBody @Valid ApprovalDecisionRequest request) {
        service.reject(approvalId, principal.employeeId(), request.comment());
        return Result.ok();
    }

    @PostMapping("/{approvalId}/delegate")
    public Result<Void> delegate(
            @AuthenticationPrincipal LoginPrincipal principal,
            @PathVariable Long approvalId,
            @RequestBody @Valid ApprovalDelegateRequest request) {
        service.delegate(approvalId, principal.employeeId(), request.targetApproverId());
        return Result.ok();
    }
}
