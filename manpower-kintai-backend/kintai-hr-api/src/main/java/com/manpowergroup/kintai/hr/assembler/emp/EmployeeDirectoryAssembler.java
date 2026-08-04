package com.manpowergroup.kintai.hr.assembler.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryFilter;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeDirectoryEntry;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeDirectoryQuery;
import com.manpowergroup.kintai.hr.controller.emp.request.EmployeeDirectoryRequest;
import com.manpowergroup.kintai.hr.controller.emp.response.EmployeeDirectoryResponse;

public final class EmployeeDirectoryAssembler {

    private EmployeeDirectoryAssembler() {
    }

    public static EmployeeDirectoryQuery toQuery(EmployeeDirectoryRequest request) {
        return new EmployeeDirectoryQuery(
            request.companyId(),
            request.keyword(),
            request.nodeId(),
            request.gradeId(),
            request.status()
        );
    }

    public static EmployeeDirectoryFilter toEmployeeFilter(EmployeeDirectoryQuery query) {
        return new EmployeeDirectoryFilter(
            query.keyword(),
            query.nodeId(),
            query.gradeId(),
            query.status()
        );
    }

    public static EmployeeDirectoryEntry toEntry(
        com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryResponse response) {

        return new EmployeeDirectoryEntry(
            response.employeeId(),
            response.employeeCode(),
            response.displayName(),
            response.displayNameKana(),
            response.email(),
            response.companyId(),
            response.companyName(),
            response.nodeId(),
            response.nodeName(),
            response.gradeId(),
            response.gradeName(),
            response.phone(),
            response.gender()
        );
    }

    public static EmployeeDirectoryResponse toResponse(EmployeeDirectoryEntry entry) {
        return new EmployeeDirectoryResponse(
            entry.employeeId(),
            entry.employeeCode(),
            entry.displayName(),
            entry.displayNameKana(),
            entry.email(),
            entry.companyId(),
            entry.companyName(),
            entry.nodeId(),
            entry.nodeName(),
            entry.gradeId(),
            entry.gradeName(),
            entry.phone(),
            entry.gender()
        );
    }

    public static JoinPageResult<EmployeeDirectoryResponse> toResponse(
        JoinPageResult<EmployeeDirectoryEntry> result) {

        return JoinPageResult.of(
            result.getRecords().stream()
                .map(EmployeeDirectoryAssembler::toResponse)
                .toList(),
            result.getTotal(),
            result.getPageNum(),
            result.getPageSize()
        );
    }
}
