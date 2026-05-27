package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 承認ステップ明細
@Data
@Accessors(chain = true)
@TableName("wf_approval_step")
public class WfApprovalStep {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 承認フローID
    private Long approvalId;

    // ステップ番号
    private Integer step;

    // 承認者社員ID
    private Long approverId;

    // ステータス
    private String status;

    // 承認コメント
    private String comment;

    // 承認日時
    private LocalDateTime approvedAt;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}

