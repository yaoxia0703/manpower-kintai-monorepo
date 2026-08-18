package com.manpowergroup.kintai.hr.application.service.impl.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import com.manpowergroup.kintai.hr.application.port.emp.*;
import com.manpowergroup.kintai.hr.application.service.emp.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDirectoryProvider employeeDirectoryProvider;
    private final EmployeeRegisterProvider employeeRegisterProvider;
    private final EmployeeRegisterValidationProvider employeeRegisterValidationProvider;


    @Override
    public JoinPageResult<EmployeeDirectoryEntry> pageDirectory(EmployeeDirectoryQuery query, int page, int size) {
        return employeeDirectoryProvider.pageDirectory(query, page, size);
    }

    @Override
    @Transactional
    public EmployeeRegisterResult registerEmployee(EmployeeRegisterEntry entry, Long operatorId) {

        validateTargetCompany(operatorId, entry.companyId());
        validateOnboardingReferences(entry);

        return employeeRegisterProvider.registerEmployee(entry);
    }


    private void validateTargetCompany(Long operatorEmployeeId, Long targetCompanyId) {

        final var operator = employeeRegisterValidationProvider.getOperator(operatorEmployeeId).orElseThrow(() -> BizException.withDetail(
            ErrorCode.FORBIDDEN, "操作者情報が取得できません"));
        final boolean superAdmin = employeeRegisterValidationProvider.hasSuperAdminRole(operatorEmployeeId);
        if (targetCompanyId == null) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "companyId is required");
        }
        if (!superAdmin && !Objects.equals(operator.companyId(), targetCompanyId)) {
            throw BizException.withDetail(ErrorCode.FORBIDDEN, "HR can only onboard employees in own company");
        }

        if (!employeeRegisterValidationProvider.companyExists(targetCompanyId)) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "companyId is invalid");
        }
    }

    public void validateOnboardingReferences(EmployeeRegisterEntry entry) {
        final var nodeId = entry.nodeId();
        final var gradeId = entry.gradeId();
        final var companyId = entry.companyId();
        if (!employeeRegisterValidationProvider.nodeBelongsToCompany(nodeId, companyId)) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "nodeId is invalid for company");
        }
        if (!employeeRegisterValidationProvider.gradeBelongsToCompany(gradeId, companyId)) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR, "gradeId is invalid for company");
        }
    }
}
