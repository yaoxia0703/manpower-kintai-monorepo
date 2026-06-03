package com.manpowergroup.kintai.system.application.assembler.hr;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.application.dto.hr.EmployeeOnboardingRequest;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;

import java.util.List;

public final class EmployeeOnboardingAssembler {

    private EmployeeOnboardingAssembler() {
    }

    public static EmpEmployee toEmployee(EmployeeOnboardingRequest request, Long operatorEmployeeId) {
        return new EmpEmployee()
                .setCompanyId(request.getCompanyId())
                .setEmployeeCode(request.getEmployeeCode())
                .setLastName(request.getLastName())
                .setFirstName(request.getFirstName())
                .setLastNameKana(request.getLastNameKana())
                .setFirstNameKana(request.getFirstNameKana())
                .setEmail(request.getEmail())
                .setPhone(request.getPhone())
                .setGender(request.getGender())
                .setHireDate(request.getHireDate())
                .setStatus(Status.ENABLED)
                .setCreatedBy(operatorEmployeeId)
                .setUpdatedBy(operatorEmployeeId);
    }

    public static EmpAccount toAccount(EmployeeOnboardingRequest request, Long employeeId, Long operatorEmployeeId) {
        return new EmpAccount()
                .setEmployeeId(employeeId)
                .setUsername(request.getUsername())
                .setStatus(Status.ENABLED)
                .setCreatedBy(operatorEmployeeId)
                .setUpdatedBy(operatorEmployeeId);
    }

    public static EmpEmployeePosition toPosition(EmployeeOnboardingRequest request, Long employeeId, Long operatorEmployeeId) {
        return new EmpEmployeePosition()
                .setEmployeeId(employeeId)
                .setCompanyId(request.getCompanyId())
                .setNodeId(request.getNodeId())
                .setGradeId(request.getGradeId())
                .setIsPrimary(1)
                .setStartDate(request.getHireDate())
                .setStatus(Status.ENABLED)
                .setCreatedBy(operatorEmployeeId)
                .setUpdatedBy(operatorEmployeeId);
    }

    public static List<SysEmployeeRole> toEmployeeRoles(EmployeeOnboardingRequest request, Long employeeId, Long operatorEmployeeId) {
        return request.getRoleIds().stream()
                .map(roleId -> new SysEmployeeRole()
                        .setEmployeeId(employeeId)
                        .setCompanyId(request.getCompanyId())
                        .setRoleId(roleId)
                        .setStartDate(request.getHireDate())
                        .setCreatedBy(operatorEmployeeId)
                        .setUpdatedBy(operatorEmployeeId))
                .toList();
    }
}
