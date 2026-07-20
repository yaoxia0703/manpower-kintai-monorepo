package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class EmployeeApprovalDelegateValidatorTest {

    @Test
    void acceptsEnabledEmployeeFromApprovalCompany() {
        EmpEmployeeService employeeService = Mockito.mock(EmpEmployeeService.class);
        ApprovalManagerEligibility eligibility = Mockito.mock(ApprovalManagerEligibility.class);
        EmployeeApprovalDelegateValidator validator = new EmployeeApprovalDelegateValidator(employeeService, eligibility);
        when(employeeService.getById(30L)).thenReturn(employee(10L, Status.ENABLED));
        when(eligibility.canApprove(30L)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validateTarget(30L, 10L));
    }

    @Test
    void rejectsEmployeeFromAnotherCompanyOrDisabledEmployee() {
        EmpEmployeeService employeeService = Mockito.mock(EmpEmployeeService.class);
        ApprovalManagerEligibility eligibility = Mockito.mock(ApprovalManagerEligibility.class);
        EmployeeApprovalDelegateValidator validator = new EmployeeApprovalDelegateValidator(employeeService, eligibility);
        when(employeeService.getById(30L)).thenReturn(employee(11L, Status.ENABLED));
        assertThrows(BizException.class, () -> validator.validateTarget(30L, 10L));

        when(employeeService.getById(30L)).thenReturn(employee(10L, Status.DISABLED));
        assertThrows(BizException.class, () -> validator.validateTarget(30L, 10L));
    }

    @Test
    void rejectsEmployeeWithoutApprovalAuthority() {
        EmpEmployeeService employeeService = Mockito.mock(EmpEmployeeService.class);
        ApprovalManagerEligibility eligibility = Mockito.mock(ApprovalManagerEligibility.class);
        EmployeeApprovalDelegateValidator validator = new EmployeeApprovalDelegateValidator(employeeService, eligibility);
        when(employeeService.getById(30L)).thenReturn(employee(10L, Status.ENABLED));
        when(eligibility.canApprove(30L)).thenReturn(false);

        assertThrows(BizException.class, () -> validator.validateTarget(30L, 10L));
    }

    private EmpEmployee employee(Long companyId, Status status) {
        return EmpEmployee.create(
                companyId, "E030", "Delegate", "User", null, null,
                "delegate@example.com", null, null, null, null, null, status)
                .setId(30L);
    }
}
