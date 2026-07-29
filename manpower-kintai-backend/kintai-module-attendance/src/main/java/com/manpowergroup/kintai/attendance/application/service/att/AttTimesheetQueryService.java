package com.manpowergroup.kintai.attendance.application.service.att;

import com.manpowergroup.kintai.attendance.application.dto.timesheet.response.TimesheetMonthResponse;
import com.manpowergroup.kintai.attendance.application.query.timesheet.TimesheetMonthQuery;

public interface AttTimesheetQueryService {

    /**
     * 指定社員・対象年月の勤怠表を取得する。
     *
     * @param query 処理対象の月次勤怠表の検索条件
     * @return 取得した月次勤怠表レスポンス
     */
    TimesheetMonthResponse getMonthlyTimesheet(TimesheetMonthQuery query);
}
