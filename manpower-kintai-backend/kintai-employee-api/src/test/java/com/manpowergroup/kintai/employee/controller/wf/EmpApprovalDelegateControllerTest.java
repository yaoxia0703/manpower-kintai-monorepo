package com.manpowergroup.kintai.employee.controller.wf;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.adapter.wf.ApprovalManagerEligibility;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmpApprovalDelegateControllerTest {

    @Test
    void searchesCandidatesInsideAuthenticatedEmployeesCompany() {
        EmpEmployeeService employeeService = Mockito.mock(EmpEmployeeService.class);
        ApprovalManagerEligibility eligibility = Mockito.mock(ApprovalManagerEligibility.class);
        EmpEmployee current = employee(20L, 3L, "JP-MGR-001", "田中", "太郎");
        EmpEmployee candidate = employee(30L, 3L, "JP-MGR-002", "鈴木", "一郎");
        PageResult<EmpEmployee> page = new PageResult<>();
        page.setRecords(List.of(candidate));
        page.setTotal(1);
        page.setPage(1);
        page.setSize(20);
        page.setPages(1);
        when(employeeService.getById(20L)).thenReturn(current);
        when(employeeService.searchByName(3L, "JP-MGR", PageRequest.of(1, 20))).thenReturn(page);
        when(eligibility.canApprove(30L)).thenReturn(true);

        var response = new EmpApprovalDelegateController(employeeService, eligibility)
                .search(new LoginPrincipal(20L, 200L), "JP-MGR", 1, 20);

        assertEquals("JP-MGR-002", response.getData().getRecords().getFirst().employeeCode());
        assertEquals("鈴木 一郎", response.getData().getRecords().getFirst().displayName());
        verify(employeeService).searchByName(3L, "JP-MGR", PageRequest.of(1, 20));
    }

    private EmpEmployee employee(Long id, Long companyId, String code, String lastName, String firstName) {
        EmpEmployee employee = EmpEmployee.create(
                companyId, code, lastName, firstName, null, null,
                code + "@example.com", null, null, null,
                LocalDate.of(2020, 1, 1), null, null);
        employee.setId(id);
        return employee;
    }
}
