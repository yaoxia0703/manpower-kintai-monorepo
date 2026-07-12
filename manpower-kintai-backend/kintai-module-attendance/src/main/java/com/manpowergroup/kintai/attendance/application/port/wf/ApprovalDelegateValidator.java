package com.manpowergroup.kintai.attendance.application.port.wf;

public interface ApprovalDelegateValidator {

    void validateTarget(Long employeeId, Long companyId);
}
