package com.manpowergroup.kintai.attendance.application.dto.att.response;

import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AttRequestResponse(
        Long id,
        RequestType requestType,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal days,
        Integer minutes,
        String reason,
        ApprovalStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AttRequestResponse from(AttRequest request) {
        return new AttRequestResponse(
                request.getId(),
                request.getRequestType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getDays(),
                request.getMinutes(),
                request.getReason(),
                request.getStatus(),
                request.getCreatedAt(),
                request.getUpdatedAt());
    }
}
