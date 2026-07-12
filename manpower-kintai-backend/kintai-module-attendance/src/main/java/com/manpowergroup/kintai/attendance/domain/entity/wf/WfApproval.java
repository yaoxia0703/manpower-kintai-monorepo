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
 * 申請全体の承認進行状況を管理する承認フロー集約である。
 */
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("wf_approval")
public class WfApproval {

    @TableId(type = IdType.AUTO)
    @Setter
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

    /** 指定された総ステップ数で承認フローを開始する。 */
    public static WfApproval start(Long requestId, String requestType, Long applicantId,
                                   Long companyId, Integer totalSteps, Long actorId) {
        return new WfApproval()
                .setRequestId(requestId)
                .setRequestType(requestType)
                .setApplicantId(applicantId)
                .setCompanyId(companyId)
                .setCurrentStep(1)
                .setTotalSteps(totalSteps)
                .setStatus(ApprovalStatus.PENDING)
                .setEscalated(0)
                .setCreatedBy(actorId)
                .setUpdatedBy(actorId);
    }

    /** 現在のステップを承認し、次ステップへ進むかフローを完了する。 */
    public void approveCurrentStep(LocalDateTime approvedAt, Long actorId) {
        requirePending();
        if (currentStep < totalSteps) {
            this.currentStep = currentStep + 1;
            clearEscalation();
            this.updatedBy = actorId;
            return;
        }
        this.status = ApprovalStatus.APPROVED;
        this.completedAt = approvedAt;
        this.updatedBy = actorId;
    }

    /** 承認待ちのフローを却下して完了する。 */
    public void reject(LocalDateTime rejectedAt, Long actorId) {
        requirePending();
        this.status = ApprovalStatus.REJECTED;
        this.completedAt = rejectedAt;
        this.updatedBy = actorId;
    }

    /** 承認待ちのフローを取り消して完了する。 */
    public void cancel(LocalDateTime cancelledAt, Long actorId) {
        requirePending();
        this.status = ApprovalStatus.CANCELLED;
        this.completedAt = cancelledAt;
        this.updatedBy = actorId;
    }

    /** 現在の承認先を指定社員へエスカレーションする。 */
    public void escalateTo(Long employeeId, LocalDateTime escalatedAt, Long actorId) {
        requirePending();
        this.escalated = 1;
        this.escalatedTo = employeeId;
        this.escalatedAt = escalatedAt;
        this.updatedBy = actorId;
    }

    private void clearEscalation() {
        this.escalated = 0;
        this.escalatedAt = null;
        this.escalatedTo = null;
    }

    private void requirePending() {
        if (status != ApprovalStatus.PENDING) {
            throw BizException.withDetail(ErrorCode.CONFLICT, "Only pending approvals can change status");
        }
    }
}
