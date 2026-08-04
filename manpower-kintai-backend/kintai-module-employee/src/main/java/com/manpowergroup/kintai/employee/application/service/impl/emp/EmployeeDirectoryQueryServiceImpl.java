package com.manpowergroup.kintai.employee.application.service.impl.emp;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryFilter;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryResponse;
import com.manpowergroup.kintai.employee.application.service.emp.EmployeeDirectoryQueryService;
import com.manpowergroup.kintai.employee.domain.repository.emp.EmpEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDirectoryQueryServiceImpl
    implements EmployeeDirectoryQueryService {

    private final EmpEmployeeRepository employeeRepository;

    @Override
    public JoinPageResult<EmployeeDirectoryResponse> pageDirectory(
        EmployeeDirectoryFilter filter,
        int page,
        int size) {

        return employeeRepository.pageDirectory(filter, page, size);
    }
}
