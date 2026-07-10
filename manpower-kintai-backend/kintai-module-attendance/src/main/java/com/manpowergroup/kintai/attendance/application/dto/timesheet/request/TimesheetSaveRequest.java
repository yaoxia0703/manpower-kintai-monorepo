package com.manpowergroup.kintai.attendance.application.dto.timesheet.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "勤務表保存リクエスト")
public record TimesheetSaveRequest(
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "勤務日", example = "2026-07-10")
        LocalDate workDate,

        @NotNull
        @Schema(description = "出勤区分（ATTENDANCE_TYPE参照）", example = "OFFICE")
        AttendanceType attendanceType,

        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "出勤時刻", example = "09:00")
        LocalTime clockIn,

        @JsonFormat(pattern = "HH:mm")
        @Schema(description = "退勤時刻", example = "18:00")
        LocalTime clockOut,

        @Min(0)
        @Max(480)
        @Schema(description = "休憩時間（分）", example = "60")
        Integer breakMinutes,

        @Size(max = 255)
        @Schema(description = "備考", example = "通常勤務")
        String remark
) {
}
