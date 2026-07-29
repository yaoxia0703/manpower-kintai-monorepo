package com.manpowergroup.kintai.attendance.application.service.wf;

public interface ApprovalDecisionService {

    /**
     * 対象の申請を承認する。
     *
     * @param approvalId 対象の承認ID
     * @param approverId 承認者の社員ID
     * @param comment 承認判断に関するコメント
     */
    void approve(Long approvalId, Long approverId, String comment);

    /**
     * 対象の申請を却下する。
     *
     * @param approvalId 対象の承認ID
     * @param approverId 承認者の社員ID
     * @param comment 承認判断に関するコメント
     */
    void reject(Long approvalId, Long approverId, String comment);

    /**
     * 対象の承認権限を別の社員へ委譲する。
     *
     * @param approvalId 対象の承認ID
     * @param approverId 承認者の社員ID
     * @param targetApproverId 委譲先の承認者ID
     */
    void delegate(Long approvalId, Long approverId, Long targetApproverId);
}
