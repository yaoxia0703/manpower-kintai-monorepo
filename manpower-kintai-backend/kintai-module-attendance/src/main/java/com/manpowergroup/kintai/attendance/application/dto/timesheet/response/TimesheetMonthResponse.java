package com.manpowergroup.kintai.attendance.application.dto.timesheet.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "勤務表月次レスポンス")
public class TimesheetMonthResponse {

    @Schema(description = "対象年", example = "2026")
    private int year;

    @Schema(description = "対象月", example = "7")
    private int month;

    @Schema(description = "日別勤務表")
    private List<TimesheetDayResponse> days;

    @Schema(description = "出勤日数", example = "20")
    private int workDays;

    @Schema(description = "総労働時間（分）", example = "9600")
    private int totalWorkMinutes;

    @Schema(description = "総残業時間（分）", example = "600")
    private int totalOvertimeMinutes;
}
