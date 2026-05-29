package com.manpowergroup.kintai.attendance.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimesheetSaveRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    private AttendanceType attendanceType;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime clockIn;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime clockOut;

    private Integer breakMinutes;

    private String remark;
}
