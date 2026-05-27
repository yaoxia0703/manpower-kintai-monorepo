package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// 各種申請
@Data
@Accessors(chain = true)
@TableName("att_request")
public class AttRequest {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 申請者社員ID
    private Long employeeId;

    // 会社ID
    private Long companyId;

    // 申請タイプ（REQUEST_TYPE参照）
    private String requestType;

    // 開始日
    private LocalDate startDate;

    // 終了日
    private LocalDate endDate;

    // 開始時刻（残業申請等）
    private LocalTime startTime;

    // 終了時刻（残業申請等）
    private LocalTime endTime;

    // 申請日数
    private BigDecimal days;

    // 申請時間（分）
    private Integer minutes;

    // 申請理由
    private String reason;

    // 承認ステータス（APPROVAL_STATUS参照）
    private String status;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}

