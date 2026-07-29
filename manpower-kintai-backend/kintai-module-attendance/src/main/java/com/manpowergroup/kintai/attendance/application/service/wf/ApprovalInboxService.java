package com.manpowergroup.kintai.attendance.application.service.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;

import java.util.List;

public interface ApprovalInboxService {

    /**
     * 指定された条件に一致する承認待ち一覧を一覧取得する。
     *
     * @param approverId 承認者の社員ID
     * @return 承認待ち情報一覧。該当しない場合は空リスト
     */
    List<ApprovalInboxItem> listPending(Long approverId);
}
