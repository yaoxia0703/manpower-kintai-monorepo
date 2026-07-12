package com.manpowergroup.kintai.attendance.domain.repository.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApproval;

import java.util.Optional;

public interface WfApprovalRepository {

    Optional<WfApproval> findByIdForUpdate(Long approvalId);

    Optional<WfApproval> findByRequestIdForUpdate(Long requestId);

    WfApproval save(WfApproval approval);

    WfApproval update(WfApproval approval);
}
