package com.manpowergroup.kintai.attendance.application.command.timesheet;

import com.manpowergroup.kintai.common.enums.AttendanceType;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimesheetSaveCommand(
        // 社員ID
        Long employeeId,

        // 会社ID
        Long companyId,

        // 勤務日
        LocalDate workDate,

        // 出勤区分
        AttendanceType attendanceType,

        // 出勤時刻
        LocalTime clockIn,

        // 退勤時刻
        LocalTime clockOut,

        // 休憩時間（分）
        Integer breakMinutes,

        // 備考
        String remark
) {
}
