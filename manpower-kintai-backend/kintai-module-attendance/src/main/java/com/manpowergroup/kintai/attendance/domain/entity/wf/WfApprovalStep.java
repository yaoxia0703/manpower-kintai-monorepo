package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 承認フロー内の1ステップを表し、担当承認者の判断結果を保持する。
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("wf_approval_step")
public class WfApprovalStep {

    @TableId(type = IdType.AUTO)
    @Setter
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

    /** 指定承認者を持つ承認待ちステップを作成する。 */
    public static WfApprovalStep pending(Long approvalId, Integer step, Long approverId, Long actorId) {
        return new WfApprovalStep()
                .setApprovalId(approvalId)
                .setStep(step)
                .setApproverId(approverId)
                .setStatus(ApprovalStatus.PENDING)
                .setCreatedBy(actorId)
                .setUpdatedBy(actorId);
    }

    /** 承認待ちステップを承認済みにする。 */
    public void approve(String comment, LocalDateTime approvedAt, Long actorId) {
        requirePending();
        this.status = ApprovalStatus.APPROVED;
        this.comment = comment;
        this.approvedAt = approvedAt;
        this.updatedBy = actorId;
    }

    /** 承認待ちステップを却下する。 */
    public void reject(String comment, LocalDateTime rejectedAt, Long actorId) {
        requirePending();
        this.status = ApprovalStatus.REJECTED;
        this.comment = comment;
        this.approvedAt = rejectedAt;
        this.updatedBy = actorId;
    }

    /** 未処理の承認ステップを取り消す。 */
    public void cancel(LocalDateTime cancelledAt, Long actorId) {
        requirePending();
        this.status = ApprovalStatus.CANCELLED;
        this.approvedAt = cancelledAt;
        this.updatedBy = actorId;
    }

    private void requirePending() {
        if (status != ApprovalStatus.PENDING) {
            throw BizException.withDetail(ErrorCode.CONFLICT, "Only pending approval steps can change status");
        }
    }
}
