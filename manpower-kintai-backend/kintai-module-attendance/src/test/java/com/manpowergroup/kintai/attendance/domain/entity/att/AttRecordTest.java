package com.manpowergroup.kintai.attendance.domain.entity.att;

import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttRecordTest {

    @Test
    void recalculateDerivesWorkAndOvertimeMinutes() {
        AttRecord record = new AttRecord()
                .setClockIn(LocalTime.of(9, 0))
                .setClockOut(LocalTime.of(19, 0));

        record.recalculate(60);

        assertEquals(540, record.getWorkMinutes());
        assertEquals(60, record.getOvertimeMinutes());
    }

    @Test
    void recalculateRejectsClockOutBeforeClockIn() {
        AttRecord record = new AttRecord()
                .setClockIn(LocalTime.of(18, 0))
                .setClockOut(LocalTime.of(9, 0));

        assertThrows(BizException.class, () -> record.recalculate(0));
    }

    @Test
    void recalculateRejectsBreakLongerThanWorkSpan() {
        AttRecord record = new AttRecord()
                .setClockIn(LocalTime.of(9, 0))
                .setClockOut(LocalTime.of(10, 0));

        assertThrows(BizException.class, () -> record.recalculate(60));
    }
}
