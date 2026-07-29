package com.manpowergroup.kintai.attendance.domain.repository.att;

import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;

import java.time.LocalDate;
import java.util.Map;

public interface AttRecordRepository {
    /**
     * 指定期間の勤怠記録を日付ごとに取得する。
     *
     * @param employeeId 対象の社員ID
     * @param companyId 対象の会社ID
     * @param start 対象期間の開始日
     * @param end 対象期間の終了日
     * @return 勤務日をキーとする勤怠記録のマップ。該当しない場合は空のマップ
     */
    Map<LocalDate, AttRecord> findAttRecordMap(Long employeeId,Long companyId,LocalDate start,LocalDate end);

    /**
     * 指定社員の勤怠記録を取得する。
     *
     * @param employeeId 対象の社員ID
     * @param companyId 対象の会社ID
     * @param workDate 対象の勤務日
     * @return 取得した勤怠記録
     */
    AttRecord findAttRecord(Long employeeId,Long companyId,LocalDate workDate);

    /**
     * 指定社員の勤怠記録を取得する。
     *
     * @param recordId 対象の勤怠記録ID
     * @param employeeId 対象の社員ID
     * @return 取得した勤怠記録
     */
    AttRecord findAttRecord(Long recordId,Long employeeId);

    /**
     * 勤怠記録を更新する。
     *
     * @param attRecord 保存対象の勤怠記録
     */
    void updateAttRecordById(AttRecord attRecord);

    /**
     * 勤怠記録を保存する。
     *
     * @param attRecord 保存対象の勤怠記録
     */
    void saveAttRecord(AttRecord attRecord);

    /**
     * 指定IDの勤怠記録を削除する。
     *
     * @param recordId 対象の勤怠記録ID
     */
    void deleteAttRecordById(Long recordId);
}
