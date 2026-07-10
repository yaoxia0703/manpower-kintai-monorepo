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

// 各種申請
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("att_request")
public class AttRequest {

    @TableId(type = IdType.AUTO)
    // 申請ID
    private Long id;

    // 申請者社員ID
    private Long employeeId;

    // 会社ID
    private Long companyId;

    // 申請タイプ（REQUEST_TYPE参照）
    private String requestType;

    // 開始日
    private LocalDate startDate;

    // 終了日
    private LocalDate endDate;

    // 開始時刻（残業申請等）
    private LocalTime startTime;

    // 終了時刻（残業申請等）
    private LocalTime endTime;

    // 申請日数
    private BigDecimal days;

    // 申請時間（分）
    private Integer minutes;

    // 申請理由
    private String reason;

    // 承認ステータス（APPROVAL_STATUS参照）
    private ApprovalStatus status;

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 更新者ID
    private Long updatedBy;

    // 更新日時
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 論理削除（0=有効 1=削除）
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
