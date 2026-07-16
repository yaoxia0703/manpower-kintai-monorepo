package com.manpowergroup.kintai.attendance.application.dto.wf.response;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;
import com.manpowergroup.kintai.common.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApprovalRuleResponse(
        Long id,
        Long companyId,
        RequestType requestType,
        ApprovalStopCondition stopCondition,
        String stopGradeLevel,
        String stopDeptFunc,
        BigDecimal amountThreshold,
        Integer sort,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ApprovalRuleResponse from(WfApprovalRule rule) {
        return new ApprovalRuleResponse(
                rule.getId(), rule.getCompanyId(), rule.getRequestType(),
                rule.getStopCondition(), rule.getStopGradeLevel(), rule.getStopDeptFunc(),
                rule.getAmountThreshold(), rule.getSort(), rule.getStatus(),
                rule.getCreatedAt(), rule.getUpdatedAt());
    }
}
