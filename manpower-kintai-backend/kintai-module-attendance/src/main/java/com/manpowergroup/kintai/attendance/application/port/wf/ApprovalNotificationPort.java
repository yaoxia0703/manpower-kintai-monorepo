package com.manpowergroup.kintai.attendance.application.port.wf;

import com.manpowergroup.kintai.attendance.domain.enums.RequestType;

/** 勤怠申請・承認イベントを通知基盤へ引き渡すポート。 */
public interface ApprovalNotificationPort {

    /**
     * 勤怠申請が提出されたことを通知する。
     *
     * @param companyId 対象の会社ID
     * @param recipientId 通知先の社員ID
     * @param requestType 申請種別
     * @param requestId 対象の申請ID
     */
    void requestSubmitted(Long companyId, Long recipientId, RequestType requestType, Long requestId);

    /**
     * 勤怠申請が承認されたことを通知する。
     *
     * @param companyId 対象の会社ID
     * @param recipientId 通知先の社員ID
     * @param requestType 申請種別
     * @param requestId 対象の申請ID
     */
    void requestApproved(Long companyId, Long recipientId, RequestType requestType, Long requestId);

    /**
     * 勤怠申請が却下されたことを通知する。
     *
     * @param companyId 対象の会社ID
     * @param recipientId 通知先の社員ID
     * @param requestType 申請種別
     * @param requestId 対象の申請ID
     */
    void requestRejected(Long companyId, Long recipientId, RequestType requestType, Long requestId);

    /**
     * 勤怠申請が取り消されたことを通知する。
     *
     * @param companyId 対象の会社ID
     * @param recipientId 通知先の社員ID
     * @param requestType 申請種別
     * @param requestId 対象の申請ID
     */
    void requestCancelled(Long companyId, Long recipientId, RequestType requestType, Long requestId);

    /**
     * 承認権限が委譲されたことを通知する。
     *
     * @param companyId 対象の会社ID
     * @param recipientId 通知先の社員ID
     * @param requestType 申請種別
     * @param requestId 対象の申請ID
     */
    void approvalDelegated(Long companyId, Long recipientId, RequestType requestType, Long requestId);
}
