package com.manpowergroup.kintai.employee.controller.wf;

public record ApprovalDelegateCandidateResponse(
        Long employeeId,
        String employeeCode,
        String displayName
) {
}
