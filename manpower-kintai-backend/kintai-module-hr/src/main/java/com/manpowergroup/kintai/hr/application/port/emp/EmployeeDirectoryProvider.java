package com.manpowergroup.kintai.hr.application.port.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;

public interface EmployeeDirectoryProvider {

    JoinPageResult<EmployeeDirectoryEntry> pageDirectory(EmployeeDirectoryQuery query, int page , int size);
}
