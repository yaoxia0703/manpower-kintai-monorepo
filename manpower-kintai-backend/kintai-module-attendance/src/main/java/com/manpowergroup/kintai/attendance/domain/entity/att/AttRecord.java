package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// 打刻記録
@Data
@Accessors(chain = true)
@TableName("att_record")
public class AttRecord {

    @TableId(type = IdType.AUTO)
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
    private Integer status;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}

