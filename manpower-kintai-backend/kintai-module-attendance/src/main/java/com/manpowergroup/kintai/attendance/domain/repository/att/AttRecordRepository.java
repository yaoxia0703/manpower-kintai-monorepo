package com.manpowergroup.kintai.attendance.domain.repository.att;

import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;

import java.time.LocalDate;
import java.util.Map;

public interface AttRecordRepository {
    Map<LocalDate, AttRecord> findAttRecordMap(Long employeeId,Long companyId,LocalDate start,LocalDate end);

    AttRecord findAttRecord(Long employeeId,Long companyId,LocalDate workDate);

    AttRecord findAttRecord(Long recordId,Long employeeId);

    void updateAttRecordById(AttRecord attRecord);

    void saveAttRecord(AttRecord attRecord);

    void deleteAttRecordById(Long recordId);
}
