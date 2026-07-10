package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;
import com.manpowergroup.kintai.attendance.application.service.att.AttTimesheetRecordService;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import com.manpowergroup.kintai.attendance.domain.enums.AttRecordStatus;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.att.AttRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttTimesheetRecordServiceImpl implements AttTimesheetRecordService {

    private final AttRecordMapper recordMapper;

    @Override
    @Transactional
    public void saveRecord(TimesheetSaveCommand command) {
        AttRecord existing = recordMapper.selectOne(
                new LambdaQueryWrapper<AttRecord>()
                        .eq(AttRecord::getEmployeeId, command.employeeId())
                        .eq(AttRecord::getCompanyId, command.companyId())
                        .eq(AttRecord::getWorkDate, command.workDate())
        );

        if (existing != null) {
            existing.setAttendanceType(command.attendanceType())
                    .setClockIn(command.clockIn())
                    .setClockOut(command.clockOut())
                    .setRemark(command.remark())
                    .setUpdatedBy(command.employeeId());
            existing.recalculate(command.breakMinutes());
            recordMapper.updateById(existing);
            return;
        }

        AttRecord record = new AttRecord()
                .setEmployeeId(command.employeeId())
                .setCompanyId(command.companyId())
                .setWorkDate(command.workDate())
                .setAttendanceType(command.attendanceType())
                .setClockIn(command.clockIn())
                .setClockOut(command.clockOut())
                .setRemark(command.remark())
                .setStatus(AttRecordStatus.DRAFT)
                .setCreatedBy(command.employeeId())
                .setUpdatedBy(command.employeeId());
        record.recalculate(command.breakMinutes());
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
            recordMapper.deleteById(command.recordId());
        }
    }
}
