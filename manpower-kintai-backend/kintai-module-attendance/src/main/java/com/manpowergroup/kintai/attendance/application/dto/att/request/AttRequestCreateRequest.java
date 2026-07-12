package com.manpowergroup.kintai.attendance.application.dto.att.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "申請リクエスト")
public record AttRequestCreateRequest(

        @Schema(description = "申請タイプ（REQUEST_TYPE参照）", example = "PAID_LEAVE")
        @NotBlank(message = "申請タイプは必須です")
        @Size(max = 50, message = "申請タイプは50文字以内にしてください")
        String requestType,

        @Schema(description = "開始日", example = "2026-07-10")
        @NotNull(message = "開始日は必須です")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @Schema(description = "終了日", example = "2026-07-10")
        @NotNull(message = "終了日は必須です")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @Schema(description = "開始時刻（残業申請等）", example = "18:00")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @Schema(description = "終了時刻（残業申請等）", example = "20:00")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,

        @Schema(description = "申請日数", example = "1.0")
        @DecimalMin(value = "0.0", inclusive = false, message = "申請日数は0より大きい値にしてください")
        BigDecimal days,

        @Schema(description = "申請時間（分）", example = "120")
        @Min(value = 0, message = "申請時間は0分以上にしてください")
        @Max(value = 1440, message = "申請時間は1440分以内にしてください")
        Integer minutes,

        @Schema(description = "申請理由", example = "私用のため")
        @Size(max = 500, message = "申請理由は500文字以内にしてください")
        String reason

) {
}
