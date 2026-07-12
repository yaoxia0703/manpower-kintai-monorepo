package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;
import com.manpowergroup.kintai.attendance.application.service.att.AttTimesheetRecordService;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import com.manpowergroup.kintai.attendance.domain.service.att.TimesheetEditLockPolicy;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.att.AttRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttTimesheetRecordServiceImpl implements AttTimesheetRecordService {

    private final AttRecordMapper recordMapper;
    private final TimesheetEditLockPolicy editLockPolicy;

    @Override
    @Transactional
    public void saveRecord(TimesheetSaveCommand command) {
        editLockPolicy.ensureEditable(
                command.employeeId(), command.companyId(), command.workDate());
        AttRecord existing = recordMapper.selectOne(
                new LambdaQueryWrapper<AttRecord>()
                        .eq(AttRecord::getEmployeeId, command.employeeId())
                        .eq(AttRecord::getCompanyId, command.companyId())
                        .eq(AttRecord::getWorkDate, command.workDate())
        );

        if (existing != null) {
            existing.updateTimesheet(
                    command.attendanceType(),
                    command.clockIn(),
                    command.clockOut(),
                    command.breakMinutes(),
                    command.remark(),
                    command.employeeId());
            recordMapper.updateById(existing);
            return;
        }

        AttRecord record = AttRecord.createDraft(
                command.employeeId(),
                command.companyId(),
                command.workDate(),
                command.attendanceType(),
                command.clockIn(),
                command.clockOut(),
                command.breakMinutes(),
                command.remark(),
                command.employeeId());
        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void deleteRecord(TimesheetDeleteCommand command) {
        AttRecord record = recordMapper.selectOne(
                new LambdaQueryWrapper<AttRecord>()
                        .eq(AttRecord::getId, command.recordId())
                        .eq(AttRecord::getEmployeeId, command.employeeId())
        );
        if (record != null) {
            editLockPolicy.ensureEditable(
                    record.getEmployeeId(), record.getCompanyId(), record.getWorkDate());
            record.ensureDeletable();
            recordMapper.deleteById(command.recordId());
        }
    }
}
