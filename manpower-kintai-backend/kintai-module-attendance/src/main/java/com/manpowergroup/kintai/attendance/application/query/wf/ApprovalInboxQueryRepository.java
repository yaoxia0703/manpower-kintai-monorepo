package com.manpowergroup.kintai.attendance.application.query.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;

import java.util.List;

public interface ApprovalInboxQueryRepository {

    List<ApprovalInboxItem> listCurrentPendingByApprover(Long approverId);
}
