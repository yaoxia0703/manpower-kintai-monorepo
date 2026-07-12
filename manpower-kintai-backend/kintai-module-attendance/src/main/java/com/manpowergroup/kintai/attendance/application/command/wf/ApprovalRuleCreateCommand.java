package com.manpowergroup.kintai.attendance.application.command.wf;

import com.manpowergroup.kintai.common.enums.Status;

import java.math.BigDecimal;

public record ApprovalRuleCreateCommand(
        Long companyId,
        String requestType,
        String stopCondition,
        String stopGradeLevel,
        String stopDeptFunc,
        BigDecimal amountThreshold,
        Integer sort,
        Status status
) {
}
