package com.manpowergroup.kintai.attendance.application.assembler.timesheet;

import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;
import com.manpowergroup.kintai.attendance.application.dto.timesheet.request.TimesheetSaveRequest;
import com.manpowergroup.kintai.attendance.application.query.timesheet.TimesheetMonthQuery;

public final class TimesheetAssembler {

    private TimesheetAssembler() {
    }

    public static TimesheetMonthQuery toMonthQuery(Long employeeId, Long companyId, int year, int month) {
        return new TimesheetMonthQuery(employeeId, companyId, year, month);
    }

    public static TimesheetSaveCommand toSaveCommand(Long employeeId, Long companyId, TimesheetSaveRequest request) {
        return new TimesheetSaveCommand(
                employeeId,
                companyId,
                request.workDate(),
                request.attendanceType(),
                request.clockIn(),
                request.clockOut(),
                request.breakMinutes(),
                request.remark()
        );
    }

    public static TimesheetDeleteCommand toDeleteCommand(Long employeeId, Long recordId) {
        return new TimesheetDeleteCommand(employeeId, recordId);
    }
}
