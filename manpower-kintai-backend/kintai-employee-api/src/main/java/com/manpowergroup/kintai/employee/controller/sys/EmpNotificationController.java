package com.manpowergroup.kintai.employee.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.application.dto.sys.request.SysNotificationMarkReadRequest;
import com.manpowergroup.kintai.system.application.dto.sys.response.SysNotificationResponse;
import com.manpowergroup.kintai.system.application.service.sys.SysNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 社員本人の通知を参照・既読化する API。 */
@RestController
@RequestMapping("/employee/notifications")
@RequiredArgsConstructor
public class EmpNotificationController {

    private final SysNotificationService notificationService;

    @GetMapping("/unread-count")
    public Result<Long> countUnread(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(notificationService.countUnread(principal.employeeId()));
    }

    @GetMapping("/unread")
    public Result<PageResult<SysNotificationResponse>> listUnread(
            @AuthenticationPrincipal LoginPrincipal principal, PageRequest pageRequest) {
        return Result.ok(notificationService.pageUnread(principal.employeeId(), pageRequest));
    }

    @PutMapping("/read")
    public Result<Void> markAsRead(
            @AuthenticationPrincipal LoginPrincipal principal,
            @Valid @RequestBody SysNotificationMarkReadRequest request) {
        notificationService.markAsRead(principal.employeeId(), request.ids());
        return Result.ok();
    }
}
