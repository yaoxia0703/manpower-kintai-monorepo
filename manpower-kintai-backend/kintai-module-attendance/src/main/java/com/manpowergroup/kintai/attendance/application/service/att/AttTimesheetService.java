package com.manpowergroup.kintai.attendance.application.service.att;

import com.manpowergroup.kintai.attendance.application.dto.TimesheetMonthResponse;
import com.manpowergroup.kintai.attendance.application.dto.TimesheetSaveRequest;

// 勤務表サービス
public interface AttTimesheetService {

    // 月次勤務表を取得
    TimesheetMonthResponse getMonthlyTimesheet(Long employeeId, Long companyId, int year, int month);

    // 1日分の記録を保存（新規 or 更新）
    void saveRecord(Long employeeId, Long companyId, TimesheetSaveRequest request);

    // 打刻記録を削除
    void deleteRecord(Long employeeId, Long recordId);
}
