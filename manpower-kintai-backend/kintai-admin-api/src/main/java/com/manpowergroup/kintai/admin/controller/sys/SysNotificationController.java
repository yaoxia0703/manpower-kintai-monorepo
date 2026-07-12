package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.application.assembler.sys.SysNotificationAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.request.SysNotificationCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.SysNotificationMarkReadRequest;
import com.manpowergroup.kintai.system.application.dto.sys.response.SysNotificationResponse;
import com.manpowergroup.kintai.system.application.service.sys.SysNotificationService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/sys/notification")
@RequiredArgsConstructor
public class SysNotificationController {
    private final SysNotificationService sysNotificationService;

    @PostMapping
    public Result<SysNotificationResponse> create(@Valid @RequestBody SysNotificationCreateRequest request) {
        return Result.ok(SysNotificationAssembler.toResponse(
                sysNotificationService.create(SysNotificationAssembler.toCommand(request))));
    }

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

    @PutMapping("/read")
    public Result<Void> markAsRead(
            @AuthenticationPrincipal LoginPrincipal principal,
            @Valid @RequestBody SysNotificationMarkReadRequest request) {
        sysNotificationService.markAsRead(principal.employeeId(), request.ids());
        return Result.ok();
    }
}
