package com.manpowergroup.kintai.attendance.application.port.wf;

/** 勤怠申請・承認イベントを通知基盤へ引き渡すポート。 */
public interface ApprovalNotificationPort {

    void requestSubmitted(Long companyId, Long recipientId, String requestType, Long requestId);

    void requestApproved(Long companyId, Long recipientId, String requestType, Long requestId);

    void requestRejected(Long companyId, Long recipientId, String requestType, Long requestId);

    void requestCancelled(Long companyId, Long recipientId, String requestType, Long requestId);

    void approvalDelegated(Long companyId, Long recipientId, String requestType, Long requestId);
}
