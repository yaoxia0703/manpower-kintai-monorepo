package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manpowergroup.kintai.attendance.domain.enums.AttRecordStatus;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 1日単位の勤怠記録を表し、勤務時間の計算と提出状態の遷移を管理する。
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("att_record")
public class AttRecord {

    public static final int STANDARD_WORK_MINUTES = 480;

    @TableId(type = IdType.AUTO)
    @Setter
    // 打刻記録ID
    private Long id;

    // 社員ID
    private Long employeeId;

    // 会社ID
    private Long companyId;

    // 勤務日
    private LocalDate workDate;

    // 出勤時刻
    private LocalTime clockIn;

    // 退勤時刻
    private LocalTime clockOut;

    // 出勤区分（ATTENDANCE_TYPE参照）
    private AttendanceType attendanceType;

    // 実労働時間（分）
    private Integer workMinutes;

    // 残業時間（分）
    private Integer overtimeMinutes;

    // 備考
    private String remark;

    // ステータス（0=未承認 1=承認済 2=否認）
    private AttRecordStatus status;

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

    /** 未提出の勤怠記録を作成し、休憩時間を差し引いた勤務時間を計算する。 */
    public static AttRecord createDraft(Long employeeId, Long companyId, LocalDate workDate,
                                        AttendanceType attendanceType, LocalTime clockIn,
                                        LocalTime clockOut, Integer breakMinutes,
                                        String remark, Long actorId) {
        AttRecord record = new AttRecord()
                .setEmployeeId(employeeId)
                .setCompanyId(companyId)
                .setWorkDate(workDate)
                .setAttendanceType(attendanceType)
                .setClockIn(clockIn)
                .setClockOut(clockOut)
                .setRemark(remark)
                .setStatus(AttRecordStatus.DRAFT)
                .setCreatedBy(actorId)
                .setUpdatedBy(actorId);
        record.recalculate(breakMinutes);
        return record;
    }

    /** 下書き状態の勤怠内容を更新し、勤務時間と残業時間を再計算する。 */
    public void updateTimesheet(AttendanceType attendanceType, LocalTime clockIn,
                                LocalTime clockOut, Integer breakMinutes,
                                String remark, Long actorId) {
        requireStatus(AttRecordStatus.DRAFT, "edit");
        this.attendanceType = attendanceType;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.remark = remark;
        this.updatedBy = actorId;
        recalculate(breakMinutes);
    }

    /** 下書きの勤怠を提出済みにする。 */
    public void submit(Long actorId) {
        requireStatus(AttRecordStatus.DRAFT, "submit");
        this.status = AttRecordStatus.SUBMITTED;
        this.updatedBy = actorId;
    }

    /** 提出済みの勤怠を下書きへ戻す。 */
    public void reopen(Long actorId) {
        requireStatus(AttRecordStatus.SUBMITTED, "reopen");
        this.status = AttRecordStatus.DRAFT;
        this.updatedBy = actorId;
    }

    /** 提出済みの勤怠を確定し、以後の再編集を禁止する。 */
    public void lock(Long actorId) {
        requireStatus(AttRecordStatus.SUBMITTED, "lock");
        this.status = AttRecordStatus.LOCKED;
        this.updatedBy = actorId;
    }

    /** 現在の状態で削除可能かを検証する。 */
    public void ensureDeletable() {
        requireStatus(AttRecordStatus.DRAFT, "delete");
    }

    private void requireStatus(AttRecordStatus expected, String action) {
        if (status != expected) {
            throw BizException.withDetail(ErrorCode.CONFLICT,
                    "attendance record must be " + expected + " to " + action);
        }
    }

    /** 現在の打刻時刻と休憩時間から勤務時間および残業時間を再計算する。 */
    public void recalculate(Integer breakMinutes) {
        if (clockIn == null || clockOut == null) {
            this.workMinutes = null;
            this.overtimeMinutes = 0;
            return;
        }

        int actualBreakMinutes = breakMinutes == null ? 0 : breakMinutes;
        long spannedMinutes = Duration.between(clockIn, clockOut).toMinutes();
        if (spannedMinutes <= 0) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "clockOut must be after clockIn");
        }
        if (actualBreakMinutes >= spannedMinutes) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "breakMinutes must be less than working span minutes");
        }

        this.workMinutes = (int) Math.max(0, spannedMinutes - actualBreakMinutes);
        this.overtimeMinutes = Math.max(0, this.workMinutes - STANDARD_WORK_MINUTES);
    }

    /** 保存済みの勤務時間から休憩時間を逆算する。 */
    public int calculateBreakMinutes() {
        if (clockIn == null || clockOut == null || workMinutes == null) {
            return 0;
        }
        long spannedMinutes = Duration.between(clockIn, clockOut).toMinutes();
        return Math.max(0, (int) spannedMinutes - workMinutes);
    }
}
