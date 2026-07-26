package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;
import com.manpowergroup.kintai.attendance.application.service.att.AttTimesheetRecordService;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRecordRepository;
import com.manpowergroup.kintai.attendance.domain.service.att.TimesheetEditLockPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttTimesheetRecordServiceImpl implements AttTimesheetRecordService {

    private final AttRecordRepository attRecordRepository;
    private final TimesheetEditLockPolicy editLockPolicy;

    @Override
    @Transactional
    public void saveRecord(TimesheetSaveCommand command) {
        editLockPolicy.ensureEditable(
            command.employeeId(), command.companyId(), command.workDate());

        AttRecord existing = attRecordRepository.findAttRecord(command.employeeId(), command.companyId(), command.workDate());

        if (existing != null) {
            existing.updateTimesheet(
                command.attendanceType(),
                command.clockIn(),
                command.clockOut(),
                command.breakMinutes(),
                command.remark(),
                command.employeeId());
            attRecordRepository.updateAttRecordById(existing);
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
        attRecordRepository.saveAttRecord(record);
    }

    @Override
    @Transactional
    public void deleteRecord(TimesheetDeleteCommand command) {
        AttRecord record = attRecordRepository.findAttRecord(command.recordId(), command.employeeId());
        if (record != null) {
            editLockPolicy.ensureEditable(
                record.getEmployeeId(), record.getCompanyId(), record.getWorkDate());
            record.ensureDeletable();
            attRecordRepository.deleteAttRecordById(command.recordId());
        }
    }
}
