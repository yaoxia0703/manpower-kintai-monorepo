package com.manpowergroup.kintai.attendance.application.service.wf;

public interface ApprovalDecisionService {

    void approve(Long approvalId, Long approverId, String comment);

    void reject(Long approvalId, Long approverId, String comment);

    void delegate(Long approvalId, Long approverId, Long targetApproverId);
}
