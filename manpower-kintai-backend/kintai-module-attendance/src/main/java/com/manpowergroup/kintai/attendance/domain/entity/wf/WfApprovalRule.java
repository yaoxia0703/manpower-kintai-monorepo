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
    // 承認ルールID
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

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 更新者ID
    private Long updatedBy;

    // 更新日時
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 論理削除（0=有効 1=削除）
    @TableLogic
    private Integer isDeleted;
}

