package com.manpowergroup.kintai.attendance.domain.service.att;

import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRequestRepository;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class TimesheetEditLockPolicyTest {

    @Test
    void returnsEveryCoveredDateForActiveLeaveRequest() {
        AttRequestRepository repository = Mockito.mock(AttRequestRepository.class);
        TimesheetEditLockPolicy policy = new TimesheetEditLockPolicy(repository);
        AttRequest request = leave(
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12));
        when(repository.listByEmployee(1L)).thenReturn(List.of(request));

        var locks = policy.findLocks(
                1L, 10L, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 31));

        assertEquals(3, locks.size());
        assertEquals(request, locks.get(LocalDate.of(2026, 7, 11)));
    }

    @Test
    void rejectsEditingCoveredDate() {
        AttRequestRepository repository = Mockito.mock(AttRequestRepository.class);
        TimesheetEditLockPolicy policy = new TimesheetEditLockPolicy(repository);
        when(repository.listByEmployee(1L)).thenReturn(List.of(leave(
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12))));

        assertThrows(BizException.class,
                () -> policy.ensureEditable(1L, 10L, LocalDate.of(2026, 7, 11)));
    }

    private AttRequest leave(LocalDate start, LocalDate end) {
        return AttRequest.create(
                1L, 10L, "PAID_LEAVE", start, end,
                null, null, BigDecimal.ONE, null, "leave");
    }
}
