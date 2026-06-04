package com.manpowergroup.kintai.attendance.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manpowergroup.kintai.attendance.domain.enums.AttRecordStatus;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
public class TimesheetDayDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    private String dayOfWeek;

    private boolean weekend;

    private boolean holiday;

    private AttendanceType attendanceType;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime clockIn;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime clockOut;

    private Integer breakMinutes;

    private Integer workMinutes;

    private Integer overtimeMinutes;

    private String remark;

    private AttRecordStatus status;

    private Long recordId;
}
