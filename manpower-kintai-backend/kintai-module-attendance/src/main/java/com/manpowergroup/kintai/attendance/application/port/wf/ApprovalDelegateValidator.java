package com.manpowergroup.kintai.attendance.application.port.wf;

public interface ApprovalDelegateValidator {

    /**
     * 指定された対象が操作可能か検証する。
     *
     * @param employeeId 対象の社員ID
     * @param companyId 対象の会社ID
     */
    void validateTarget(Long employeeId, Long companyId);
}
