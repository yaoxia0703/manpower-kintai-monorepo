package com.manpowergroup.kintai.attendance.application.dto.wf.response;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDetailHeader {

    private Long approvalId;
    private Long requestId;
    private String requestType;
    private Long applicantId;
    private Integer currentStep;
    private Integer totalSteps;
    private ApprovalStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LocalDateTime submittedAt;
    private LocalDateTime completedAt;
}
