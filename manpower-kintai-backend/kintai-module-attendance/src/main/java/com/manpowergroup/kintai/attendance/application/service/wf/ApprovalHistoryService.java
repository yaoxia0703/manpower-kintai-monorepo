package com.manpowergroup.kintai.attendance.application.service.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailResponse;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;

import java.util.List;

public interface ApprovalHistoryService {

    ApprovalDetailResponse getDetail(Long viewerId, Long approvalId);

    List<ApprovalHistoryItem> listHistory(Long viewerId);
}
