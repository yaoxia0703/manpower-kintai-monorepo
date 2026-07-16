package com.manpowergroup.kintai.attendance.application.dto.wf.response;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHistoryItem {

    private Long approvalId;
    private Long requestId;
    private RequestType requestType;
    private Long applicantId;
    private String applicantEmployeeCode;
    private String applicantName;
    private ApprovalStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime completedAt;
}
