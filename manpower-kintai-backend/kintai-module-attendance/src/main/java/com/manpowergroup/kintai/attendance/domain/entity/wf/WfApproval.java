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

// 承認フロー
@Data
@Accessors(chain = true)
@TableName("wf_approval")
public class WfApproval {

    @TableId(type = IdType.AUTO)
    // 承認フローID
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
    private ApprovalStatus status;

    // エスカレーションフラグ（0=通常 1=本社）
    private Integer escalated;

    // エスカレーション日時
    private LocalDateTime escalatedAt;

    // エスカレーション先社員ID
    private Long escalatedTo;

    // 承認完了日時
    private LocalDateTime completedAt;

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
