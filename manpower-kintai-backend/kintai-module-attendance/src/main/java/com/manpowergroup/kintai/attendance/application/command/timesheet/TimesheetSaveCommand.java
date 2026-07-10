package com.manpowergroup.kintai.attendance.application.command.timesheet;

import com.manpowergroup.kintai.common.enums.AttendanceType;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimesheetSaveCommand(
        Long employeeId,
        Long companyId,
        LocalDate workDate,
        AttendanceType attendanceType,
        LocalTime clockIn,
        LocalTime clockOut,
        Integer breakMinutes,
        String remark
) {
}
