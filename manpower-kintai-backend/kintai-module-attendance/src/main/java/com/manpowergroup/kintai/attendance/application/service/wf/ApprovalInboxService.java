package com.manpowergroup.kintai.attendance.application.service.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;

import java.util.List;

public interface ApprovalInboxService {

    List<ApprovalInboxItem> listPending(Long approverId);
}
