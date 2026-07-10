package com.manpowergroup.kintai.attendance.application.query.timesheet;

public record TimesheetMonthQuery(
        Long employeeId,
        Long companyId,
        int year,
        int month
) {
}
