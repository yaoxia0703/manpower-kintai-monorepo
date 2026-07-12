package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manpowergroup.kintai.attendance.domain.enums.MonthlySummaryStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 社員の月次勤怠集計と確定状態を管理する。
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("att_monthly_summary")
public class AttMonthlySummary {

    @TableId(type = IdType.AUTO)
    @Setter
    // 月次集計ID
    private Long id;

    // 社員ID
    private Long employeeId;

    // 会社ID
    private Long companyId;

    // 対象年月（YYYY-MM）
    private String summaryMonth;

    // 出勤日数
    private Integer workDays;

    // 欠勤日数
    private Integer absentDays;

    // 有給取得日数
    private BigDecimal paidLeaveDays;

    // 総労働時間（分）
    private Integer totalWorkMinutes;

    // 総残業時間（分）
    private Integer totalOvertimeMinutes;

    // 遅刻回数
    private Integer lateCount;

    // 早退回数
    private Integer earlyLeaveCount;

    // ステータス（0=未確定 1=確定済）
    private MonthlySummaryStatus status;

    // 確定日時
    private LocalDateTime confirmedAt;

    // 確定者ID
    private Long confirmedBy;

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

    /** 未確定状態の月次集計を作成する。 */
    public static AttMonthlySummary createOpen(Long employeeId, Long companyId, String summaryMonth,
                                               Integer workDays, Integer absentDays,
                                               BigDecimal paidLeaveDays, Integer totalWorkMinutes,
                                               Integer totalOvertimeMinutes, Integer lateCount,
                                               Integer earlyLeaveCount) {
        return new AttMonthlySummary()
                .setEmployeeId(employeeId)
                .setCompanyId(companyId)
                .setSummaryMonth(summaryMonth)
                .setWorkDays(workDays)
                .setAbsentDays(absentDays)
                .setPaidLeaveDays(paidLeaveDays)
                .setTotalWorkMinutes(totalWorkMinutes)
                .setTotalOvertimeMinutes(totalOvertimeMinutes)
                .setLateCount(lateCount)
                .setEarlyLeaveCount(earlyLeaveCount)
                .setStatus(MonthlySummaryStatus.OPEN);
    }

    /** 未確定の月次集計を確定し、確定者と確定日時を記録する。 */
    public void confirm(Long confirmerId, LocalDateTime confirmedAt) {
        if (status != MonthlySummaryStatus.OPEN) {
            throw BizException.withDetail(ErrorCode.CONFLICT, "Only open monthly summaries can be confirmed");
        }
        this.status = MonthlySummaryStatus.CONFIRMED;
        this.confirmedBy = confirmerId;
        this.confirmedAt = confirmedAt;
    }

    /** 確定済みの月次集計を再開し、確定情報を消去する。 */
    public void reopen() {
        if (status != MonthlySummaryStatus.CONFIRMED) {
            throw BizException.withDetail(ErrorCode.CONFLICT, "Only confirmed monthly summaries can be reopened");
        }
        this.status = MonthlySummaryStatus.OPEN;
        this.confirmedBy = null;
        this.confirmedAt = null;
    }
}
