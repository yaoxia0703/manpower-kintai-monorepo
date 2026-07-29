package com.manpowergroup.kintai.attendance.application.query.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailHeader;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalStepItem;

import java.util.List;
import java.util.Optional;

public interface ApprovalHistoryQueryRepository {

    /**
     * 閲覧可能な承認詳細を取得する。
     *
     * @param approvalId 対象の承認ID
     * @param viewerId 閲覧する社員ID
     * @return 対象が存在する場合は承認詳細ヘッダーを含むOptional、存在しない場合はOptional.empty()
     */
    Optional<ApprovalDetailHeader> findAccessibleDetail(Long approvalId, Long viewerId);

    /**
     * 対象承認の承認ステップを一覧取得する。
     *
     * @param approvalId 対象の承認ID
     * @return 承認ステップ情報一覧。該当しない場合は空リスト
     */
    List<ApprovalStepItem> listSteps(Long approvalId);

    /**
     * 閲覧可能な承認履歴を一覧取得する。
     *
     * @param viewerId 閲覧する社員ID
     * @return 承認履歴情報一覧。該当しない場合は空リスト
     */
    List<ApprovalHistoryItem> listHistory(Long viewerId);
}
