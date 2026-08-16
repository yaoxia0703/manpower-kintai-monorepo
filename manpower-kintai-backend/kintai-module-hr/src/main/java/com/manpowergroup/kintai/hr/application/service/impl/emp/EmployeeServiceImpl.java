package com.manpowergroup.kintai.hr.application.service.impl.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.hr.application.port.emp.*;
import com.manpowergroup.kintai.hr.application.service.emp.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDirectoryProvider employeeDirectoryProvider;
    private final EmployeeRegisterProvider employeeRegisterProvider;
    private final EmployeeRegisterValidationPort employeeRegisterValidationPort;


    @Override
    public JoinPageResult<EmployeeDirectoryEntry> pageDirectory(EmployeeDirectoryQuery query, int page, int size) {
        return employeeDirectoryProvider.pageDirectory(query, page, size);
    }

    @Override
    public EmployeeRegisterResult registerEmployee(EmployeeRegisterEntry entry, Long employeeId) {
        //

//        validateTargetCompany(operator, command.companyId(), superAdmin);
//        validateOnboardingReferences(command);

        return employeeRegisterProvider.registerEmployee(entry, employeeId);
    }
}
