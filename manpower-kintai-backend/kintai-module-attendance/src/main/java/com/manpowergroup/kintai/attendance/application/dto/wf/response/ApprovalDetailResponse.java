package com.manpowergroup.kintai.attendance.application.dto.wf.response;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ApprovalDetailResponse(
        Long approvalId,
        Long requestId,
        RequestType requestType,
        Long applicantId,
        String applicantEmployeeCode,
        String applicantName,
        Integer currentStep,
        Integer totalSteps,
        ApprovalStatus status,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        LocalDateTime submittedAt,
        LocalDateTime completedAt,
        List<ApprovalStepItem> steps
) {
    public static ApprovalDetailResponse from(
            ApprovalDetailHeader header, List<ApprovalStepItem> steps) {
        return new ApprovalDetailResponse(
                header.getApprovalId(), header.getRequestId(), header.getRequestType(),
                header.getApplicantId(), header.getApplicantEmployeeCode(), header.getApplicantName(),
                header.getCurrentStep(), header.getTotalSteps(),
                header.getStatus(), header.getStartDate(), header.getEndDate(),
                header.getReason(), header.getSubmittedAt(), header.getCompletedAt(),
                List.copyOf(steps));
    }
}
