package com.manpowergroup.kintai.attendance.application.command.timesheet;

public record TimesheetDeleteCommand(
        Long employeeId,
        Long recordId
) {
}
