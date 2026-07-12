package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.manpowergroup.kintai.attendance.domain.enums.AttRecordStatus;
import com.manpowergroup.kintai.common.enums.AttendanceType;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttRecordTest {

    @Test
    void createDraftInitializesTimesheetFieldsAndCalculatesMinutes() {
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

        assertEquals(1L, record.getEmployeeId());
        assertEquals(10L, record.getCompanyId());
        assertEquals(LocalDate.of(2026, 7, 10), record.getWorkDate());
        assertEquals(AttendanceType.OFFICE, record.getAttendanceType());
        assertEquals(LocalTime.of(9, 0), record.getClockIn());
        assertEquals(LocalTime.of(18, 0), record.getClockOut());
        assertEquals(480, record.getWorkMinutes());
        assertEquals(0, record.getOvertimeMinutes());
        assertEquals("regular day", record.getRemark());
        assertEquals(AttRecordStatus.DRAFT, record.getStatus());
        assertEquals(1L, record.getCreatedBy());
        assertEquals(1L, record.getUpdatedBy());
    }

    @Test
    void updateTimesheetKeepsIdentityAndStatus() {
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
        record.setId(99L);

        record.updateTimesheet(
                AttendanceType.REMOTE,
                LocalTime.of(10, 0),
                LocalTime.of(19, 30),
                30,
                "remote day",
                2L);

        assertEquals(99L, record.getId());
        assertEquals(1L, record.getEmployeeId());
        assertEquals(10L, record.getCompanyId());
        assertEquals(LocalDate.of(2026, 7, 10), record.getWorkDate());
        assertEquals(AttRecordStatus.DRAFT, record.getStatus());
        assertEquals(AttendanceType.REMOTE, record.getAttendanceType());
        assertEquals(LocalTime.of(10, 0), record.getClockIn());
        assertEquals(LocalTime.of(19, 30), record.getClockOut());
        assertEquals(540, record.getWorkMinutes());
        assertEquals(60, record.getOvertimeMinutes());
        assertEquals("remote day", record.getRemark());
        assertEquals(1L, record.getCreatedBy());
        assertEquals(2L, record.getUpdatedBy());
    }

    @Test
    void recalculateDerivesWorkAndOvertimeMinutes() {
        AttRecord record = draft();
        record.updateTimesheet(
                AttendanceType.OFFICE,
                LocalTime.of(9, 0),
                LocalTime.of(19, 0),
                60,
                "long day",
                2L);

        assertEquals(540, record.getWorkMinutes());
        assertEquals(60, record.getOvertimeMinutes());
    }

    @Test
    void recalculateRejectsClockOutBeforeClockIn() {
        assertThrows(BizException.class, () -> AttRecord.createDraft(
                1L, 10L, LocalDate.of(2026, 7, 10), AttendanceType.OFFICE,
                LocalTime.of(18, 0), LocalTime.of(9, 0), 0, null, 1L));
    }

    @Test
    void recalculateRejectsBreakLongerThanWorkSpan() {
        assertThrows(BizException.class, () -> AttRecord.createDraft(
                1L, 10L, LocalDate.of(2026, 7, 10), AttendanceType.OFFICE,
                LocalTime.of(9, 0), LocalTime.of(10, 0), 60, null, 1L));
    }

    @Test
    void submitMovesDraftToSubmitted() {
        AttRecord record = draft();

        record.submit(2L);

        assertEquals(AttRecordStatus.SUBMITTED, record.getStatus());
        assertEquals(2L, record.getUpdatedBy());
    }

    @Test
    void submittedRecordCannotBeEditedOrDeleted() {
        AttRecord record = draft();
        record.submit(2L);

        assertThrows(BizException.class, () -> record.updateTimesheet(
                AttendanceType.REMOTE,
                LocalTime.of(10, 0),
                LocalTime.of(19, 0),
                60,
                "changed",
                2L));
        assertThrows(BizException.class, record::ensureDeletable);
    }

    @Test
    void reopenMovesSubmittedRecordBackToDraft() {
        AttRecord record = draft();
        record.submit(2L);

        record.reopen(3L);

        assertEquals(AttRecordStatus.DRAFT, record.getStatus());
        assertEquals(3L, record.getUpdatedBy());
    }

    @Test
    void lockMovesSubmittedRecordToLockedAndPreventsReopen() {
        AttRecord record = draft();
        record.submit(2L);

        record.lock(3L);

        assertEquals(AttRecordStatus.LOCKED, record.getStatus());
        assertEquals(3L, record.getUpdatedBy());
        assertThrows(BizException.class, () -> record.reopen(4L));
    }

    private AttRecord draft() {
        return AttRecord.createDraft(
                1L,
                10L,
                LocalDate.of(2026, 7, 10),
                AttendanceType.OFFICE,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                60,
                "regular day",
                1L);
    }
}
