package com.manpowergroup.kintai.attendance.application.command.wf;

import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;

import java.math.BigDecimal;

public record ApprovalRuleUpdateCommand(
        RequestType requestType,
        ApprovalStopCondition stopCondition,
        String stopGradeLevel,
        String stopDeptFunc,
        BigDecimal amountThreshold,
        Integer sort
) {
}
