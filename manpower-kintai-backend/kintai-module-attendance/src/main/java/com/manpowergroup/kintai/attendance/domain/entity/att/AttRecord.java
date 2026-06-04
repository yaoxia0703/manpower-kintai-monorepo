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

@Data
@Accessors(chain = true)
@TableName("att_record")
public class AttRecord {

    public static final int STANDARD_WORK_MINUTES = 480;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long employeeId;

    private Long companyId;

    private LocalDate workDate;

    private LocalTime clockIn;

    private LocalTime clockOut;

    private AttendanceType attendanceType;

    private Integer workMinutes;

    private Integer overtimeMinutes;

    private String remark;

    private AttRecordStatus status;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

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
