package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetDeleteCommand;
import com.manpowergroup.kintai.attendance.application.command.timesheet.TimesheetSaveCommand;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import com.manpowergroup.kintai.attendance.domain.service.att.TimesheetEditLockPolicy;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.att.AttRecordMapper;
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

@SuppressWarnings("unchecked")
class AttTimesheetRecordServiceImplTest {

    @Test
    void deleteRejectsSubmittedRecord() {
        AttRecordMapper mapper = Mockito.mock(AttRecordMapper.class);
        AttTimesheetRecordServiceImpl service = new AttTimesheetRecordServiceImpl(
                mapper, Mockito.mock(TimesheetEditLockPolicy.class));
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
        when(mapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(record);

        assertThrows(BizException.class,
                () -> service.deleteRecord(new TimesheetDeleteCommand(1L, 99L)));

        verify(mapper, never()).deleteById(99L);
    }

    @Test
    void saveRejectsDateLockedByLeaveRequest() {
        AttRecordMapper mapper = Mockito.mock(AttRecordMapper.class);
        TimesheetEditLockPolicy lockPolicy = Mockito.mock(TimesheetEditLockPolicy.class);
        AttTimesheetRecordServiceImpl service = new AttTimesheetRecordServiceImpl(mapper, lockPolicy);
        LocalDate workDate = LocalDate.of(2026, 7, 10);
        Mockito.doThrow(BizException.class)
                .when(lockPolicy).ensureEditable(1L, 10L, workDate);

        assertThrows(BizException.class, () -> service.saveRecord(new TimesheetSaveCommand(
                1L, 10L, workDate, AttendanceType.OFFICE,
                LocalTime.of(9, 0), LocalTime.of(18, 0), 60, null)));

        verify(mapper, never()).insert(any(AttRecord.class));
    }
}
