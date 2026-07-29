package com.manpowergroup.kintai.attendance.application.query.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;

import java.util.List;

public interface ApprovalInboxQueryRepository {

    /**
     * 指定承認者の現在の承認待ち申請を一覧取得する。
     *
     * @param approverId 承認者の社員ID
     * @return 承認待ち情報一覧。該当しない場合は空リスト
     */
    List<ApprovalInboxItem> listCurrentPendingByApprover(Long approverId);
}
