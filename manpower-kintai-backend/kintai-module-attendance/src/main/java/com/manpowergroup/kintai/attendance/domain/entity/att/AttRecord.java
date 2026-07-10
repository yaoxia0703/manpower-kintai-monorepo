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
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// 打刻記録
@Data
@Accessors(chain = true)
@TableName("att_record")
public class AttRecord {

    public static final int STANDARD_WORK_MINUTES = 480;

    @TableId(type = IdType.AUTO)
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

    public int calculateBreakMinutes() {
        if (clockIn == null || clockOut == null || workMinutes == null) {
            return 0;
        }
        long spannedMinutes = Duration.between(clockIn, clockOut).toMinutes();
        return Math.max(0, (int) spannedMinutes - workMinutes);
    }
}
