package com.manpowergroup.kintai.employee.controller.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.application.assembler.sys.SysNotificationAssembler;
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

/**
 * 社員本人の通知を参照・既読化する API。
 */
@RestController
@RequestMapping("/employee/notifications")
@RequiredArgsConstructor
public class EmpNotificationController {

    private final SysNotificationService notificationService;

    /**
     * 対象社員の未読通知件数を取得する。
     *
     * @param principal ログインユーザー情報
     * @return 未読通知件数を含むAPIレスポンス
     */
    @GetMapping("/unread-count")
    public Result<Long> countUnread(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(notificationService.countUnread(principal.employeeId()));
    }

    /**
     * 対象社員の未読通知を一覧取得する。
     *
     * @param principal ログインユーザー情報
     * @param pageRequest ページング条件
     * @return ページングされた通知レスポンス一覧を含むAPIレスポンス
     */
    @GetMapping("/unread")
    public Result<PageResult<SysNotificationResponse>> listUnread(
        @AuthenticationPrincipal LoginPrincipal principal, PageRequest pageRequest) {
        return Result.ok(notificationService.pageUnread(principal.employeeId(), pageRequest).map(SysNotificationAssembler::toResponse));
    }

    /**
     * 指定された通知を既読に更新する。
     *
     * @param principal ログインユーザー情報
     * @param request 処理対象の通知既読更新リクエスト
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/read")
    public Result<Void> markAsRead(
        @AuthenticationPrincipal LoginPrincipal principal,
        @Valid @RequestBody SysNotificationMarkReadRequest request) {
        notificationService.markAsRead(principal.employeeId(), request.ids());
        return Result.ok();
    }
}
