package com.manpowergroup.kintai.attendance.application.command.wf;

import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;
import com.manpowergroup.kintai.common.enums.Status;

import java.math.BigDecimal;

public record ApprovalRuleCreateCommand(
        Long companyId,
        RequestType requestType,
        ApprovalStopCondition stopCondition,
        String stopGradeLevel,
        String stopDeptFunc,
        BigDecimal amountThreshold,
        Integer sort,
        Status status
) {
}
