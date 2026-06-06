package com.manpowergroup.kintai.system.application.service.hr;

import com.manpowergroup.kintai.system.application.dto.hr.EmployeeOnboardingRequest;
import com.manpowergroup.kintai.system.application.dto.hr.response.EmployeeOnboardingOptionsResponse;
import com.manpowergroup.kintai.system.application.dto.hr.response.EmployeeOnboardingResponse;

public interface EmployeeOnboardingService {

    EmployeeOnboardingOptionsResponse options(Long operatorEmployeeId, Long companyId);

    EmployeeOnboardingResponse onboard(EmployeeOnboardingRequest request, Long operatorEmployeeId);
}
