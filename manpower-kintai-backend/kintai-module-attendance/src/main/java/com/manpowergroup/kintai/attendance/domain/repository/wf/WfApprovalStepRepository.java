package com.manpowergroup.kintai.attendance.domain.repository.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalStep;

import java.util.List;
import java.util.Optional;

public interface WfApprovalStepRepository {

    /**
     * 承認およびステップに対応する承認ステップを取得する。
     *
     * @param approvalId 対象の承認ID
     * @param step 対象の承認ステップ
     * @return 対象が存在する場合は承認ステップを含むOptional、存在しない場合はOptional.empty()
     */
    Optional<WfApprovalStep> findByApprovalAndStep(Long approvalId, Integer step);

    /**
     * 承認に一致する承認ステップを一覧取得する。
     *
     * @param approvalId 対象の承認ID
     * @return 承認ステップ一覧。該当しない場合は空リスト
     */
    List<WfApprovalStep> listPendingByApproval(Long approvalId);

    /**
     * 承認ステップを保存する。
     *
     * @param step 対象の承認ステップ
     * @return 保存した承認ステップ
     */
    WfApprovalStep save(WfApprovalStep step);

    /**
     * 承認ステップを更新する。
     *
     * @param step 対象の承認ステップ
     * @return 更新した承認ステップ
     */
    WfApprovalStep update(WfApprovalStep step);
}
