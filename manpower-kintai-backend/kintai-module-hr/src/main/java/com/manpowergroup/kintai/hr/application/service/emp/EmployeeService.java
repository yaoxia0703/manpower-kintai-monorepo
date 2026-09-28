package com.manpowergroup.kintai.hr.application.service.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.hr.application.port.emp.*;

public interface EmployeeService {

    JoinPageResult<EmployeeDirectoryEntry> pageDirectory(
        EmployeeDirectoryQuery query,
        int page,
        int size);

    EmployeeRegisterResult registerEmployee(EmployeeRegisterEntry entry,Long employeeId);

    EmployeeOptionsResult getEmployeeOptions(Long operatorEmployeeId,Long companyId);
}
