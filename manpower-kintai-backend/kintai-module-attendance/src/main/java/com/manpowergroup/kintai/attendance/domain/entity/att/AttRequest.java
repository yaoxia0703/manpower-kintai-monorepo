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
import java.util.Set;

/**
 * 休暇や残業などの勤怠申請を表し、申請内容と承認状態を管理する。
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("att_request")
public class AttRequest {

    private static final Set<String> TIMESHEET_LOCKING_REQUEST_TYPES = Set.of(
            "PAID_LEAVE", "SUBSTITUTE", "LEAVE_OF_ABSENCE");

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

    /** 入力期間を検証し、承認待ちの勤怠申請を作成する。 */
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
        validatePeriod(startDate, endDate);
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
                .setStatus(ApprovalStatus.PENDING)
                .setCreatedBy(employeeId)
                .setUpdatedBy(employeeId);
    }

    /** 承認待ちの申請内容を更新する。 */
    public void updateDetails(
            String requestType,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            BigDecimal days,
            Integer minutes,
            String reason,
            Long actorId
    ) {
        requirePending();
        validatePeriod(startDate, endDate);
        this.requestType = requestType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
        this.minutes = minutes;
        this.reason = reason;
        this.updatedBy = actorId;
    }

    /** テストや復元用途で承認待ち状態の申請を生成する。 */
    public static AttRequest pending() {
        return new AttRequest().setStatus(ApprovalStatus.PENDING);
    }

    public void approve() {
        approve(null);
    }

    /** 承認待ちの申請を承認済みにする。 */
    public void approve(Long actorId) {
        requirePending();
        this.status = ApprovalStatus.APPROVED;
        this.updatedBy = actorId;
    }

    public void reject() {
        reject(null);
    }

    /** 承認待ちの申請を却下する。 */
    public void reject(Long actorId) {
        requirePending();
        this.status = ApprovalStatus.REJECTED;
        this.updatedBy = actorId;
    }

    public void cancel() {
        cancel(null);
    }

    /** 承認待ちの申請を取消済みにする。 */
    public void cancel(Long actorId) {
        requirePending();
        this.status = ApprovalStatus.CANCELLED;
        this.updatedBy = actorId;
    }

    /** 承認待ちまたは承認済みの休暇申請が指定日の勤務表編集を禁止するか判定する。 */
    public boolean locksTimesheetOn(LocalDate workDate) {
        if (workDate == null || startDate == null || endDate == null) {
            return false;
        }
        boolean active = status == ApprovalStatus.PENDING || status == ApprovalStatus.APPROVED;
        boolean covered = !workDate.isBefore(startDate) && !workDate.isAfter(endDate);
        return active && covered && TIMESHEET_LOCKING_REQUEST_TYPES.contains(requestType);
    }

    private void requirePending() {
        if (status != ApprovalStatus.PENDING) {
            throw BizException.withDetail(ErrorCode.CONFLICT, "Only pending attendance requests can change status");
        }
    }

    private static void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR,
                    "attendance request endDate must not be before startDate");
        }
    }
}
