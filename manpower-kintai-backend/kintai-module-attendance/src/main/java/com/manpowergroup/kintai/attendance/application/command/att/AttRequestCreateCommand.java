package com.manpowergroup.kintai.attendance.application.command.att;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record AttRequestCreateCommand(
        // 申請者社員ID
        Long employeeId,

        // 会社ID
        Long companyId,

        // 申請タイプ（REQUEST_TYPE参照）
        String requestType,

        // 開始日
        LocalDate startDate,

        // 終了日
        LocalDate endDate,

        // 開始時刻（残業申請等）
        LocalTime startTime,

        // 終了時刻（残業申請等）
        LocalTime endTime,

        // 申請日数
        BigDecimal days,

        // 申請時間（分）
        Integer minutes,

        // 申請理由
        String reason
) {
}
