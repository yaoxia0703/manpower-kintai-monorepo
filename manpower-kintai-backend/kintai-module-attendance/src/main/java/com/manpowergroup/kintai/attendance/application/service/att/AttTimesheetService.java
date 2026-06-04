package com.manpowergroup.kintai.attendance.application.service.att;

import com.manpowergroup.kintai.attendance.application.dto.TimesheetMonthResponse;
import com.manpowergroup.kintai.attendance.application.dto.TimesheetSaveRequest;

public interface AttTimesheetService {

    TimesheetMonthResponse getMonthlyTimesheet(Long employeeId, Long companyId, int year, int month);

    void saveRecord(Long employeeId, Long companyId, TimesheetSaveRequest request);

    void deleteRecord(Long employeeId, Long recordId);
}
