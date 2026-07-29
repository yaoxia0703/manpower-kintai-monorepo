package com.manpowergroup.kintai.attendance.application.service.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailResponse;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;

import java.util.List;

public interface ApprovalHistoryService {

    /**
     * 指定された承認の詳細を取得する。
     *
     * @param viewerId 閲覧する社員ID
     * @param approvalId 対象の承認ID
     * @return 取得した承認詳細レスポンス
     */
    ApprovalDetailResponse getDetail(Long viewerId, Long approvalId);

    /**
     * 閲覧可能な承認履歴を一覧取得する。
     *
     * @param viewerId 閲覧する社員ID
     * @return 承認履歴情報一覧。該当しない場合は空リスト
     */
    List<ApprovalHistoryItem> listHistory(Long viewerId);
}
