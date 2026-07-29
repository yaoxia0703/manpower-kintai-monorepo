package com.manpowergroup.kintai.attendance.application.service.att;

import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;

public interface AttTimesheetRecordService {

    /**
     * 指定日の勤怠記録を保存する。
     *
     * @param command 処理対象の勤怠記録保存コマンド
     */
    void saveRecord(TimesheetSaveCommand command);

    /**
     * 指定日の勤怠記録を削除する。
     *
     * @param command 処理対象の勤怠記録削除コマンド
     */
    void deleteRecord(TimesheetDeleteCommand command);
}
