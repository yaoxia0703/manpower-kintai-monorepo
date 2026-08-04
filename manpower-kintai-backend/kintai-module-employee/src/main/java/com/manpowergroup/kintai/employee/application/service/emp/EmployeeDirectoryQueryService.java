package com.manpowergroup.kintai.employee.application.service.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryFilter;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryResponse;

public interface EmployeeDirectoryQueryService {

    JoinPageResult<EmployeeDirectoryResponse> pageDirectory(
        EmployeeDirectoryFilter filter,
        int page,
        int size);
}
