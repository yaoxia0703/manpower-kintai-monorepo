package com.manpowergroup.kintai.hr.application.service.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeDirectoryEntry;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeDirectoryQuery;

public interface EmployeeDirectoryService {

    JoinPageResult<EmployeeDirectoryEntry> pageDirectory(
        EmployeeDirectoryQuery query,
        int page,
        int size);
}
