package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 承認ルール定義
@Data
@Accessors(chain = true)
@TableName("wf_approval_rule")
public class WfApprovalRule {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 会社ID
    private Long companyId;

    // 申請タイプ（REQUEST_TYPE参照）
    private String requestType;

    // 終止条件（DIRECT_ONLY/REACH_GRADE/REACH_DEPARTMENT）
    private String stopCondition;

    // 終止職級（REACH_GRADE時使用）
    private String stopGradeLevel;

    // 終止部門機能（REACH_DEPARTMENT時使用）
    private String stopDeptFunc;

    // 金額閾値（NULLは常に発動）
    private BigDecimal amountThreshold;

    // 表示順
    private Integer sort;

    // ステータス
    private Status status;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}

