package com.manpowergroup.kintai.employee.application.service.hr;

import com.manpowergroup.kintai.employee.application.dto.hr.request.EmployeeOnboardingRequest;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingOptionsResponse;
import com.manpowergroup.kintai.employee.application.dto.hr.response.EmployeeOnboardingResponse;

public interface EmployeeOnboardingService {

    EmployeeOnboardingOptionsResponse options(Long operatorEmployeeId, Long companyId);

    EmployeeOnboardingResponse onboard(EmployeeOnboardingRequest request, Long operatorEmployeeId);
}
