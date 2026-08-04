package com.manpowergroup.kintai.employee.application.dto.directory;

import com.manpowergroup.kintai.common.enums.Status;

public record EmployeeDirectoryFilter(
    String keyword,
    Long nodeId,
    Long gradeId,
    Status status
) {
}
