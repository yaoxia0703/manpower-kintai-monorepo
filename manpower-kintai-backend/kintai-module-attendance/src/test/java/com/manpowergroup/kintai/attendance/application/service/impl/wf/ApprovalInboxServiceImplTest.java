package com.manpowergroup.kintai.attendance.application.service.impl.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;
import com.manpowergroup.kintai.attendance.application.query.wf.ApprovalInboxQueryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApprovalInboxServiceImplTest {

    @Test
    void listsOnlyTasksReturnedForAuthenticatedApprover() {
        ApprovalInboxQueryRepository repository = Mockito.mock(ApprovalInboxQueryRepository.class);
        ApprovalInboxServiceImpl service = new ApprovalInboxServiceImpl(repository);
        ApprovalInboxItem item = new ApprovalInboxItem(
                7L, 99L, "PAID_LEAVE", 1L, "JP-EMP-001", "山田 太郎",
                1, 2,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12),
                "leave", LocalDateTime.of(2026, 7, 9, 9, 0));
        when(repository.listCurrentPendingByApprover(20L)).thenReturn(List.of(item));

        assertEquals(List.of(item), service.listPending(20L));
        verify(repository).listCurrentPendingByApprover(20L);
    }
}
