package com.manpowergroup.kintai.attendance.application.query.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailHeader;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalStepItem;

import java.util.List;
import java.util.Optional;

public interface ApprovalHistoryQueryRepository {

    Optional<ApprovalDetailHeader> findAccessibleDetail(Long approvalId, Long viewerId);

    List<ApprovalStepItem> listSteps(Long approvalId);

    List<ApprovalHistoryItem> listHistory(Long viewerId);
}
