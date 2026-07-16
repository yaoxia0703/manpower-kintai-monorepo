package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("wf_approval_rule")
/**
 * 申請種別と金額条件に応じて承認経路の終了条件を定義する。
 */
public class WfApprovalRule {

    @TableId(type = IdType.AUTO)
    @Setter
    private Long id;

    private Long companyId;

    private RequestType requestType;

    private ApprovalStopCondition stopCondition;

    private String stopGradeLevel;

    private String stopDeptFunc;

    private BigDecimal amountThreshold;

    private Integer sort;

    private Status status;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;

    /** ルールの整合性を検証し、有効な承認ルールを作成する。 */
    public static WfApprovalRule create(Long companyId, RequestType requestType, ApprovalStopCondition stopCondition,
                                        String stopGradeLevel, String stopDeptFunc,
                                        BigDecimal amountThreshold, Integer sort, Status status) {
        validate(companyId, requestType, stopCondition, stopGradeLevel,
                stopDeptFunc, amountThreshold, sort);
        return new WfApprovalRule()
                .setCompanyId(companyId)
                .setRequestType(requestType)
                .setStopCondition(stopCondition)
                .setStopGradeLevel(stopGradeLevel)
                .setStopDeptFunc(stopDeptFunc)
                .setAmountThreshold(amountThreshold)
                .setSort(sort)
                .setStatus(status == null ? Status.ENABLED : status);
    }

    /** 会社を変更せずに承認経路条件を更新する。 */
    public void updateRule(RequestType requestType, ApprovalStopCondition stopCondition, String stopGradeLevel,
                           String stopDeptFunc, BigDecimal amountThreshold, Integer sort) {
        validate(companyId, requestType, stopCondition, stopGradeLevel,
                stopDeptFunc, amountThreshold, sort);
        this.requestType = requestType;
        this.stopCondition = stopCondition;
        this.stopGradeLevel = stopGradeLevel;
        this.stopDeptFunc = stopDeptFunc;
        this.amountThreshold = amountThreshold;
        this.sort = sort;
    }

    /** 申請種別と金額がこの有効ルールの適用条件を満たすか判定する。 */
    public boolean appliesTo(RequestType requestType, BigDecimal amount) {
        if (this.status != Status.ENABLED) {
            return false;
        }
        if (this.requestType != requestType) {
            return false;
        }
        return this.amountThreshold == null
                || (amount != null && amount.compareTo(this.amountThreshold) >= 0);
    }

    /** ルールを有効化する。 */
    public void enable() {
        this.status = Status.ENABLED;
    }

    /** ルールを無効化する。 */
    public void disable() {
        this.status = Status.DISABLED;
    }

    private static void validate(
            Long companyId,
            RequestType requestType,
            ApprovalStopCondition stopCondition,
            String stopGradeLevel,
            String stopDeptFunc,
            BigDecimal amountThreshold,
            Integer sort
    ) {
        if (companyId == null || requestType == null) {
            throw invalidRule("approval rule company and request type are required");
        }
        if (stopCondition == null) {
            throw invalidRule("unsupported approval stop condition");
        }
        if (stopCondition == ApprovalStopCondition.REACH_GRADE
                && (stopGradeLevel == null || stopGradeLevel.isBlank())) {
            throw invalidRule("grade stop condition requires target grade level");
        }
        if (stopCondition == ApprovalStopCondition.REACH_DEPARTMENT
                && (stopDeptFunc == null || stopDeptFunc.isBlank())) {
            throw invalidRule("department stop condition requires target department function");
        }
        if (amountThreshold != null && amountThreshold.signum() < 0) {
            throw invalidRule("approval rule threshold must not be negative");
        }
        if (sort == null || sort < 0) {
            throw invalidRule("approval rule sort must not be negative");
        }
    }

    private static BizException invalidRule(String detail) {
        return BizException.withDetail(ErrorCode.VALIDATION_ERROR, detail);
    }
}
