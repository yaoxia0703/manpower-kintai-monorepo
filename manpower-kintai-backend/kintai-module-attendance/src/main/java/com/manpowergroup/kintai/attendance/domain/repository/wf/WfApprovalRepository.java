package com.manpowergroup.kintai.attendance.domain.repository.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApproval;

import java.util.Optional;

public interface WfApprovalRepository {

    /**
     * 指定IDの承認を更新用ロック付きで取得する。
     *
     * @param approvalId 対象の承認ID
     * @return 対象が存在する場合は承認を含むOptional、存在しない場合はOptional.empty()
     */
    Optional<WfApproval> findByIdForUpdate(Long approvalId);

    /**
     * 指定申請の承認を更新用ロック付きで取得する。
     *
     * @param requestId 対象の申請ID
     * @return 対象が存在する場合は承認を含むOptional、存在しない場合はOptional.empty()
     */
    Optional<WfApproval> findByRequestIdForUpdate(Long requestId);

    /**
     * 承認を保存する。
     *
     * @param approval 対象の承認
     * @return 保存した承認
     */
    WfApproval save(WfApproval approval);

    /**
     * 承認を更新する。
     *
     * @param approval 対象の承認
     * @return 更新した承認
     */
    WfApproval update(WfApproval approval);
}
