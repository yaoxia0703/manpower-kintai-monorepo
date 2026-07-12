package com.manpowergroup.kintai.attendance.application.command.wf;

import java.math.BigDecimal;

public record ApprovalRuleUpdateCommand(
        String requestType,
        String stopCondition,
        String stopGradeLevel,
        String stopDeptFunc,
        BigDecimal amountThreshold,
        Integer sort
) {
}
