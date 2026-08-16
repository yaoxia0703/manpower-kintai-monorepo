package com.manpowergroup.kintai.hr.application.port.emp;

import com.manpowergroup.kintai.common.enums.Status;

import java.util.List;
import java.util.Optional;

public interface EmployeeRegisterValidationPort {

    Optional<OperatorInfo> getOperator(Long operatorEmployeeId);

    boolean companyExists(Long companyId);

    boolean nodeBelongsToCompany(
        Long nodeId,
        Long companyId
    );

    boolean gradeBelongsToCompany(
        Long gradeId,
        Long companyId
    );

    boolean hasSuperAdminRole(Long employeeId);

    boolean rolesAreEnabledForCompany(
        Long companyId,
        List<Long> roleIds
    );


    public record OperatorInfo(
        Long employeeId,
        Long companyId,
        Status status
    ) {

    }
}
