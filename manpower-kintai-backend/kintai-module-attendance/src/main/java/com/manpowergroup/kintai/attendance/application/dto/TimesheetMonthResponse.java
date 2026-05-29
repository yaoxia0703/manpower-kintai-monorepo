package com.manpowergroup.kintai.attendance.application.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TimesheetMonthResponse {

    private int year;

    private int month;

    private List<TimesheetDayDTO> days;

    private int workDays;

    private int totalWorkMinutes;

    private int totalOvertimeMinutes;
}
