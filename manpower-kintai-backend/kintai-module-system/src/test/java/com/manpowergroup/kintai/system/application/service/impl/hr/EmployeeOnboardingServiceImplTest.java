package com.manpowergroup.kintai.system.application.service.impl.hr;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.service.emp.EmpAccountService;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeePositionService;
import com.manpowergroup.kintai.system.application.service.emp.EmpEmployeeService;
import com.manpowergroup.kintai.system.application.service.sys.EmployeeRoleAssignmentService;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgCompanyMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgGradeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
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
        SysEmployeeRoleMapper employeeRoleMapper = Mockito.mock(SysEmployeeRoleMapper.class);
        SysRoleMapper roleMapper = Mockito.mock(SysRoleMapper.class);
        EmployeeOnboardingServiceImpl service = new EmployeeOnboardingServiceImpl(
                Mockito.mock(EmpEmployeeService.class),
                Mockito.mock(EmpAccountService.class),
                Mockito.mock(EmpEmployeePositionService.class),
                Mockito.mock(EmployeeRoleAssignmentService.class),
                employeeMapper,
                companyMapper,
                nodeMapper,
                gradeMapper,
                employeeRoleMapper,
                roleMapper);

        EmpEmployee operator = EmpEmployee.create(
                10L, "E001", "Operator", "User", null, null,
                "operator@example.com", null, null, null, null, null, Status.ENABLED)
                .setId(1L);
        SysEmployeeRole futureAssignment = SysEmployeeRole.assign(
                1L, 99L, 10L, LocalDate.now().plusDays(1), null);
        SysRole superAdmin = SysRole.create(10L, "SUPER_ADMIN", "Super Admin", null, 1)
                .setId(99L);

        when(employeeMapper.selectById(1L)).thenReturn(operator);
        when(employeeRoleMapper.selectList(any())).thenReturn(List.of(futureAssignment));
        when(roleMapper.selectList(any())).thenReturn(List.of(superAdmin));
        when(companyMapper.selectCount(any())).thenReturn(1L);
        when(companyMapper.selectList(any())).thenReturn(List.of());
        when(nodeMapper.selectList(any())).thenReturn(List.of());
        when(gradeMapper.selectList(any())).thenReturn(List.of());

        assertThrows(BizException.class, () -> service.options(1L, 20L));
    }
}
