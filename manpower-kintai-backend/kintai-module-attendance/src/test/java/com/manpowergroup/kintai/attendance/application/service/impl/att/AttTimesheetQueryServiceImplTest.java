package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.attendance.application.query.timesheet.TimesheetMonthQuery;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.service.att.TimesheetEditLockPolicy;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.att.AttRecordMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class AttTimesheetQueryServiceImplTest {

    @Test
    void monthlyTimesheetMarksDatesLockedByLeaveRequest() {
        AttRecordMapper mapper = Mockito.mock(AttRecordMapper.class);
        TimesheetEditLockPolicy lockPolicy = Mockito.mock(TimesheetEditLockPolicy.class);
        AttTimesheetQueryServiceImpl service = new AttTimesheetQueryServiceImpl(mapper, lockPolicy);
        LocalDate lockedDate = LocalDate.of(2026, 7, 10);
        AttRequest request = AttRequest.create(
                1L, 10L, RequestType.PAID_LEAVE, lockedDate, lockedDate,
                null, null, BigDecimal.ONE, null, "leave");
        when(mapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(List.of());
        when(lockPolicy.findLocks(
                1L, 10L, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31)))
                .thenReturn(Map.of(lockedDate, request));

        var result = service.getMonthlyTimesheet(new TimesheetMonthQuery(1L, 10L, 2026, 7));
        var day = result.getDays().stream()
                .filter(item -> lockedDate.equals(item.getWorkDate()))
                .findFirst().orElseThrow();

        assertTrue(day.isRequestLocked());
        assertEquals(RequestType.PAID_LEAVE, day.getLockingRequestType());
        assertEquals(request.getStatus(), day.getLockingRequestStatus());
    }
}
