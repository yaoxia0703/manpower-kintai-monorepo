package com.manpowergroup.kintai.attendance.application.service.att;

import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;

public interface AttTimesheetRecordService {

    void saveRecord(TimesheetSaveCommand command);

    void deleteRecord(TimesheetDeleteCommand command);
}
