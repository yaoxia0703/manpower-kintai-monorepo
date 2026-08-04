package com.manpowergroup.kintai.hr.adapter.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.employee.application.service.emp.EmployeeDirectoryQueryService;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeDirectoryEntry;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeDirectoryProvider;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeDirectoryQuery;
import com.manpowergroup.kintai.hr.assembler.emp.EmployeeDirectoryAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeDirectoryProviderAdapter
    implements EmployeeDirectoryProvider {

    private final EmployeeDirectoryQueryService employeeDirectoryQueryService;

    @Override
    public JoinPageResult<EmployeeDirectoryEntry> pageDirectory(
        EmployeeDirectoryQuery query,
        int page,
        int size) {

        var employeeFilter =
            EmployeeDirectoryAssembler.toEmployeeFilter(query);

        var employeeResult =
            employeeDirectoryQueryService.pageDirectory(
                employeeFilter,
                page,
                size);

        var entries = employeeResult.getRecords().stream()
            .map(EmployeeDirectoryAssembler::toEntry)
            .toList();

        return JoinPageResult.of(
            entries,
            employeeResult.getTotal(),
            employeeResult.getPageNum(),
            employeeResult.getPageSize()
        );
    }
}
