package com.manpowergroup.kintai.attendance.application.dto.wf.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalInboxItem {

    private Long approvalId;
    private Long requestId;
    private String requestType;
    private Long applicantId;
    private String applicantEmployeeCode;
    private String applicantName;
    private Integer currentStep;
    private Integer totalSteps;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LocalDateTime submittedAt;
}
