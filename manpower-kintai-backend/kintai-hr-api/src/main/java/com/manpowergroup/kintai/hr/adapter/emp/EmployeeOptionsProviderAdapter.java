package com.manpowergroup.kintai.hr.adapter.emp;

import com.manpowergroup.kintai.hr.application.port.emp.EmployeeOptionsProvider;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeOptionsResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeOptionsProviderAdapter  implements EmployeeOptionsProvider {

//    private final Emp
    @Override
    public EmployeeOptionsResult getEmployeeOptions(Long operatorEmployeeId, Long companyId) {
        return null;
    }
}
