package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.application.dto.sys.response.SysNotificationResponse;
import com.manpowergroup.kintai.system.application.service.sys.SysNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/sys/notification")
@RequiredArgsConstructor
public class SysNotificationController {
    private final SysNotificationService sysNotificationService;

    @GetMapping("/unread-count")
    public Result<Long> countUnread(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(sysNotificationService.countUnread(principal.employeeId()));
    }

    @GetMapping("/unread")
    public Result<PageResult<SysNotificationResponse>> listUnread(
            @AuthenticationPrincipal LoginPrincipal principal,
            PageRequest pageRequest) {
        return Result.ok(sysNotificationService.pageUnread(principal.employeeId(), pageRequest));
    }
}
