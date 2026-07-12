package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.attendance.application.port.wf.ApprovalDelegateValidator;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SystemApprovalDelegateValidator implements ApprovalDelegateValidator {

    private final EmpEmployeeService employeeService;
    private final ApprovalManagerEligibility approvalManagerEligibility;

    @Override
    public void validateTarget(Long employeeId, Long companyId) {
        EmpEmployee employee = employeeService.getById(employeeId);
        if (!Objects.equals(employee.getCompanyId(), companyId)) {
            throw invalidTarget("delegate approver belongs to another company");
        }
        if (employee.getStatus() != Status.ENABLED) {
            throw invalidTarget("delegate approver is not enabled");
        }
        if (!approvalManagerEligibility.canApprove(employeeId)) {
            throw invalidTarget("delegate employee does not have approval authority");
        }
    }

    private BizException invalidTarget(String detail) {
        return BizException.withDetail(ErrorCode.CONFLICT, detail);
    }
}
