package com.manpowergroup.kintai.attendance.application.assembler.timesheet;

import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;
import com.manpowergroup.kintai.attendance.application.dto.timesheet.request.TimesheetSaveRequest;
import com.manpowergroup.kintai.attendance.application.query.timesheet.TimesheetMonthQuery;

public final class TimesheetAssembler {

    private TimesheetAssembler() {
    }

    /**
     * 入力データを月次勤怠表の検索条件へ変換する。
     *
     * @param employeeId 対象の社員ID
     * @param companyId 対象の会社ID
     * @param year 対象年
     * @param month 対象月
     * @return 変換後の月次勤怠表の検索条件
     */
    public static TimesheetMonthQuery toMonthQuery(Long employeeId, Long companyId, int year, int month) {
        return new TimesheetMonthQuery(employeeId, companyId, year, month);
    }

    /**
     * 入力データを勤怠記録保存コマンドへ変換する。
     *
     * @param employeeId 対象の社員ID
     * @param companyId 対象の会社ID
     * @param request 変換対象の勤怠表保存リクエスト
     * @return 変換後の勤怠記録保存コマンド
     */
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

    /**
     * 入力データを勤怠記録削除コマンドへ変換する。
     *
     * @param employeeId 対象の社員ID
     * @param recordId 対象の勤怠記録ID
     * @return 変換後の勤怠記録削除コマンド
     */
    public static TimesheetDeleteCommand toDeleteCommand(Long employeeId, Long recordId) {
        return new TimesheetDeleteCommand(employeeId, recordId);
    }
}
