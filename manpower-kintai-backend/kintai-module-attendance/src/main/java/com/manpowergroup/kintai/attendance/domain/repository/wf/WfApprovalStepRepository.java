package com.manpowergroup.kintai.attendance.domain.repository.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalStep;

import java.util.List;
import java.util.Optional;

public interface WfApprovalStepRepository {

    Optional<WfApprovalStep> findByApprovalAndStep(Long approvalId, Integer step);

    List<WfApprovalStep> listPendingByApproval(Long approvalId);

    WfApprovalStep save(WfApprovalStep step);

    WfApprovalStep update(WfApprovalStep step);
}
