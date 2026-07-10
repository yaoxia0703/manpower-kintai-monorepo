package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 承認ステップ明細
@Data
@Accessors(chain = true)
@TableName("wf_approval_step")
public class WfApprovalStep {

    @TableId(type = IdType.AUTO)
    // 承認ステップID
    private Long id;

    // 承認フローID
    private Long approvalId;

    // ステップ番号
    private Integer step;

    // 承認者社員ID
    private Long approverId;

    // ステータス
    private ApprovalStatus status;

    // 承認コメント
    private String comment;

    // 承認日時
    private LocalDateTime approvedAt;

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
