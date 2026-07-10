package com.manpowergroup.kintai.attendance.application.dto.timesheet.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.manpowergroup.kintai.attendance.domain.enums.AttRecordStatus;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Accessors(chain = true)
@Schema(description = "勤務表日別レスポンス")
public class TimesheetDayResponse {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "勤務日", example = "2026-07-10")
    private LocalDate workDate;

    @Schema(description = "曜日", example = "金")
    private String dayOfWeek;

    @Schema(description = "週末フラグ", example = "false")
    private boolean weekend;

    @Schema(description = "祝日フラグ", example = "false")
    private boolean holiday;

    @Schema(description = "出勤区分（ATTENDANCE_TYPE参照）", example = "OFFICE")
    private AttendanceType attendanceType;

    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "出勤時刻", example = "09:00")
    private LocalTime clockIn;

    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "退勤時刻", example = "18:00")
    private LocalTime clockOut;

    @Schema(description = "休憩時間（分）", example = "60")
    private Integer breakMinutes;

    @Schema(description = "実労働時間（分）", example = "480")
    private Integer workMinutes;

    @Schema(description = "残業時間（分）", example = "30")
    private Integer overtimeMinutes;

    @Schema(description = "備考", example = "通常勤務")
    private String remark;

    @Schema(description = "打刻記録ステータス", example = "DRAFT")
    private AttRecordStatus status;

    @Schema(description = "打刻記録ID", example = "1")
    private Long recordId;
}
