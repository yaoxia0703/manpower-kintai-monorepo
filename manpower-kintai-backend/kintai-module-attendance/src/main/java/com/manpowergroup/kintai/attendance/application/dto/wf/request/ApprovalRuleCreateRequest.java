package com.manpowergroup.kintai.attendance.application.dto.wf.request;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ApprovalRuleCreateRequest(
        @NotNull Long companyId,
        @NotNull RequestType requestType,
        @NotNull ApprovalStopCondition stopCondition,
        @Size(max = 10) String stopGradeLevel,
        @Size(max = 50) String stopDeptFunc,
        @DecimalMin("0.0") BigDecimal amountThreshold,
        @NotNull @Min(0) Integer sort,
        Status status
) {
}
