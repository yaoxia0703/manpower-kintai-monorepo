package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 承認フロー
@Data
@Accessors(chain = true)
@TableName("wf_approval")
public class WfApproval {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 申請ID
    private Long requestId;

    // 申請タイプ
    private String requestType;

    // 申請者社員ID
    private Long applicantId;

    // 会社ID
    private Long companyId;

    // 現在の承認ステップ
    private Integer currentStep;

    // 総承認ステップ数
    private Integer totalSteps;

    // 承認ステータス
    private String status;

    // エスカレーションフラグ（0=通常 1=本社）
    private Integer escalated;

    // エスカレーション日時
    private LocalDateTime escalatedAt;

    // エスカレーション先社員ID
    private Long escalatedTo;

    // 承認完了日時
    private LocalDateTime completedAt;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}

