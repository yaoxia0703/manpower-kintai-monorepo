package com.manpowergroup.kintai.attendance.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimesheetSaveRequest {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    @NotNull
    private AttendanceType attendanceType;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime clockIn;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime clockOut;

    @Min(0)
    @Max(480)
    private Integer breakMinutes;

    @Size(max = 255)
    private String remark;
}
