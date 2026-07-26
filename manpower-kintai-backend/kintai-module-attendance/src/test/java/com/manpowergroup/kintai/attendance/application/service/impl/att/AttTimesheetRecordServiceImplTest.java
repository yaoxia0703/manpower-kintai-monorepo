package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRecordRepository;
import com.manpowergroup.kintai.attendance.domain.service.att.TimesheetEditLockPolicy;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AttTimesheetRecordServiceImplTest {

    @Test
    void deleteRejectsSubmittedRecord() {
        AttRecordRepository recordRepository = Mockito.mock(AttRecordRepository.class);
        AttTimesheetRecordServiceImpl service = new AttTimesheetRecordServiceImpl(
                recordRepository, Mockito.mock(TimesheetEditLockPolicy.class));
        AttRecord record = AttRecord.createDraft(
                1L,
                10L,
                LocalDate.of(2026, 7, 10),
                AttendanceType.OFFICE,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                60,
                "regular day",
                1L);
        record.submit(1L);
        when(recordRepository.findAttRecord(99L, 1L)).thenReturn(record);

        assertThrows(BizException.class,
                () -> service.deleteRecord(new TimesheetDeleteCommand(1L, 99L)));

        verify(recordRepository, never()).deleteAttRecordById(99L);
    }

    @Test
    void saveRejectsDateLockedByLeaveRequest() {
        AttRecordRepository recordRepository = Mockito.mock(AttRecordRepository.class);
        TimesheetEditLockPolicy lockPolicy = Mockito.mock(TimesheetEditLockPolicy.class);
        AttTimesheetRecordServiceImpl service = new AttTimesheetRecordServiceImpl(recordRepository, lockPolicy);
        LocalDate workDate = LocalDate.of(2026, 7, 10);
        Mockito.doThrow(BizException.class)
                .when(lockPolicy).ensureEditable(1L, 10L, workDate);

        assertThrows(BizException.class, () -> service.saveRecord(new TimesheetSaveCommand(
                1L, 10L, workDate, AttendanceType.OFFICE,
                LocalTime.of(9, 0), LocalTime.of(18, 0), 60, null)));

        verify(recordRepository, never()).saveAttRecord(any(AttRecord.class));
    }
}
