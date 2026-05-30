package com.manpowergroup.kintai.system.application.dto.hr;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeOnboardingResponse {

    private Long employeeId;
    private Long accountId;
    private Long positionId;
    private String employeeCode;
    private String displayName;
    private String email;
}
