package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalNotificationPort;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.system.application.command.sys.SysNotificationCreateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysNotificationService;
import com.manpowergroup.kintai.system.domain.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** 勤怠申請イベントをシステム通知へ変換するアダプター。 */
@Component
@RequiredArgsConstructor
public class SystemApprovalNotificationAdapter implements ApprovalNotificationPort {

    private final SysNotificationService notificationService;

    @Override
    public void requestSubmitted(
            Long companyId, Long recipientId, RequestType requestType, Long requestId) {
        create(companyId, recipientId, NotificationType.REQUEST_SUBMITTED,
                "勤怠申請が提出されました", "承認待ちの勤怠申請があります。",
                requestType, requestId);
    }

    @Override
    public void requestApproved(
            Long companyId, Long recipientId, RequestType requestType, Long requestId) {
        create(companyId, recipientId, NotificationType.REQUEST_APPROVED,
                "勤怠申請が承認されました", "提出した勤怠申請が承認されました。",
                requestType, requestId);
    }

    @Override
    public void requestRejected(
            Long companyId, Long recipientId, RequestType requestType, Long requestId) {
        create(companyId, recipientId, NotificationType.REQUEST_REJECTED,
                "勤怠申請が却下されました", "提出した勤怠申請が却下されました。",
                requestType, requestId);
    }

    @Override
    public void requestCancelled(
            Long companyId, Long recipientId, RequestType requestType, Long requestId) {
        create(companyId, recipientId, NotificationType.REQUEST_CANCELLED,
                "勤怠申請が取り消されました", "承認待ちの勤怠申請が取り消されました。",
                requestType, requestId);
    }

    @Override
    public void approvalDelegated(
            Long companyId, Long recipientId, RequestType requestType, Long requestId) {
        create(companyId, recipientId, NotificationType.REQUEST_SUBMITTED,
                "勤怠申請の承認が委譲されました", "承認を担当する勤怠申請があります。",
                requestType, requestId);
    }

    private void create(
            Long companyId, Long recipientId, NotificationType type,
            String title, String content, RequestType requestType, Long requestId) {
        notificationService.create(new SysNotificationCreateCommand(
                companyId, recipientId, type, title, content, requestType.getCode(), requestId));
    }
}
