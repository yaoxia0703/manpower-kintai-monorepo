package com.manpowergroup.kintai.attendance.application.command.timesheet;

public record TimesheetDeleteCommand(
        // 社員ID
        Long employeeId,

        // 勤怠記録ID
        Long recordId
) {
}
