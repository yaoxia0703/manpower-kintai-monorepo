package com.manpowergroup.kintai.attendance.application.service.att;

import com.manpowergroup.kintai.attendance.application.dto.timesheet.response.TimesheetMonthResponse;
import com.manpowergroup.kintai.attendance.application.query.timesheet.TimesheetMonthQuery;

public interface AttTimesheetQueryService {

    TimesheetMonthResponse getMonthlyTimesheet(TimesheetMonthQuery query);
}
