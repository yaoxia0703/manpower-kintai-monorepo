package com.manpowergroup.kintai.employee.application.service.impl.hr;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.employee.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.employee.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.system.application.service.sys.EmployeeRoleAssignmentService;
import com.manpowergroup.kintai.system.application.service.sys.RoleAccessService;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgCompanyMapper;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgGradeMapper;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgNodeMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EmployeeOnboardingServiceImplTest {

    @Test
    void futureSuperAdminAssignmentDoesNotAuthorizeCrossCompanyOptions() {
        EmpEmployeeMapper employeeMapper = Mockito.mock(EmpEmployeeMapper.class);
        OrgCompanyMapper companyMapper = Mockito.mock(OrgCompanyMapper.class);
        OrgNodeMapper nodeMapper = Mockito.mock(OrgNodeMapper.class);
        OrgGradeMapper gradeMapper = Mockito.mock(OrgGradeMapper.class);
        RoleAccessService roleAccessService = Mockito.mock(RoleAccessService.class);
        EmployeeOnboardingServiceImpl service = new EmployeeOnboardingServiceImpl(
                Mockito.mock(EmpEmployeeService.class),
                Mockito.mock(EmpAccountService.class),
                Mockito.mock(EmpEmployeePositionService.class),
                Mockito.mock(EmployeeRoleAssignmentService.class),
                roleAccessService,
                employeeMapper,
                companyMapper,
                nodeMapper,
                gradeMapper);

        EmpEmployee operator = EmpEmployee.create(
                10L, "E001", "Operator", "User", null, null,
                "operator@example.com", null, null, null, null, null, Status.ENABLED)
                .setId(1L);
        when(employeeMapper.selectById(1L)).thenReturn(operator);
        when(roleAccessService.employeeHasActiveRole(
                Mockito.eq(1L), Mockito.eq("SUPER_ADMIN"), Mockito.any())).thenReturn(false);
        when(companyMapper.selectCount(any())).thenReturn(1L);
        when(companyMapper.selectList(any())).thenReturn(List.of());
        when(nodeMapper.selectList(any())).thenReturn(List.of());
        when(gradeMapper.selectList(any())).thenReturn(List.of());

        assertThrows(BizException.class, () -> service.options(1L, 20L));
    }
}
