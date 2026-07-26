package com.manpowergroup.kintai.attendance.infrastructure.repository.att;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRecordRepository;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.att.AttRecordMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AttRecordRepositoryImpl implements AttRecordRepository {
    private final AttRecordMapper attRecordMapper;

    public AttRecordRepositoryImpl(AttRecordMapper attRecordMapper) {
        this.attRecordMapper = attRecordMapper;
    }

    @Override
    public Map<LocalDate, AttRecord> findAttRecordMap(Long employeeId, Long companyId, LocalDate start, LocalDate end) {
        return attRecordMapper.selectList(
            new LambdaQueryWrapper<AttRecord>()
                .eq(AttRecord::getEmployeeId, employeeId)
                .eq(AttRecord::getCompanyId, companyId)
                .between(AttRecord::getWorkDate, start, end)
        ).stream().collect(Collectors.toMap(AttRecord::getWorkDate, record -> record));

    }

    @Override
    public AttRecord findAttRecord(Long employeeId, Long companyId, LocalDate workDate) {
        return attRecordMapper.selectOne(
            new LambdaQueryWrapper<AttRecord>()
                .eq(AttRecord::getEmployeeId, employeeId)
                .eq(AttRecord::getCompanyId, companyId)
                .eq(AttRecord::getWorkDate, workDate)
        );
    }

    @Override
    public AttRecord findAttRecord(Long recordId, Long employeeId) {
        return attRecordMapper.selectOne(
            new LambdaQueryWrapper<AttRecord>()
                .eq(AttRecord::getId, recordId)
                .eq(AttRecord::getEmployeeId, employeeId)
        );
    }

    @Override
    public void updateAttRecordById(AttRecord attRecord) {
        attRecordMapper.updateById(attRecord);
    }

    @Override
    public void saveAttRecord(AttRecord attRecord) {
        attRecordMapper.insert(attRecord);
    }

    @Override
    public void deleteAttRecordById(Long recordId) {
        attRecordMapper.deleteById(recordId);
    }
}
