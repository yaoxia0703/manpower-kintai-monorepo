package com.manpowergroup.kintai.attendance.application.service.att;

import com.manpowergroup.kintai.attendance.application.command.att.AttRequestCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.att.AttRequestUpdateCommand;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;

import java.util.List;

public interface AttRequestService {

    /**
     * 勤怠申請を新規作成する。
     *
     * @param command 処理対象の勤怠申請作成コマンド
     * @return 保存した勤怠申請
     */
    AttRequest create(AttRequestCreateCommand command);

    /**
     * 勤怠申請を更新する。
     *
     * @param command 処理対象の勤怠申請更新コマンド
     * @return 更新した勤怠申請
     */
    AttRequest update(AttRequestUpdateCommand command);

    /**
     * 勤怠申請を取り消す。
     *
     * @param employeeId 対象の社員ID
     * @param requestId 対象の申請ID
     */
    void cancel(Long employeeId, Long requestId);

    /**
     * 社員に一致する勤怠申請を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 勤怠申請一覧。該当しない場合は空リスト
     */
    List<AttRequest> listByEmployee(Long employeeId);
}
