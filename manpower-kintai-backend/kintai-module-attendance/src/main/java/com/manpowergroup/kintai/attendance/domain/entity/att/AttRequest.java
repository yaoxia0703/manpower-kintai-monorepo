package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("att_request")
public class AttRequest {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long employeeId;

    private Long companyId;

    private String requestType;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal days;

    private Integer minutes;

    private String reason;

    private ApprovalStatus status;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;

    public static AttRequest create(
            Long employeeId,
            Long companyId,
            String requestType,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            BigDecimal days,
            Integer minutes,
            String reason
    ) {
        return new AttRequest()
                .setEmployeeId(employeeId)
                .setCompanyId(companyId)
                .setRequestType(requestType)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setDays(days)
                .setMinutes(minutes)
                .setReason(reason)
                .setStatus(ApprovalStatus.PENDING);
    }

    public static AttRequest pending() {
        return new AttRequest().setStatus(ApprovalStatus.PENDING);
    }

    public void approve() {
        requirePending();
        this.status = ApprovalStatus.APPROVED;
    }

    public void reject() {
        requirePending();
        this.status = ApprovalStatus.REJECTED;
    }

    public void cancel() {
        requirePending();
        this.status = ApprovalStatus.CANCELLED;
    }

    private void requirePending() {
        if (status != ApprovalStatus.PENDING) {
            throw BizException.withDetail(ErrorCode.CONFLICT, "Only pending attendance requests can change status");
        }
    }
}
