package com.manpowergroup.kintai.attendance.application.service.impl.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailHeader;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalStepItem;
import com.manpowergroup.kintai.attendance.application.query.wf.ApprovalHistoryQueryRepository;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApprovalHistoryServiceImplTest {

    @Test
    void accessibleDetailIncludesOrderedApprovalSteps() {
        ApprovalHistoryQueryRepository repository = Mockito.mock(ApprovalHistoryQueryRepository.class);
        ApprovalHistoryServiceImpl service = new ApprovalHistoryServiceImpl(repository);
        ApprovalDetailHeader header = header();
        List<ApprovalStepItem> steps = List.of(
                new ApprovalStepItem(1, 20L, "JP-MGR-001", "田中 太郎", ApprovalStatus.APPROVED,
                        "ok", LocalDateTime.of(2026, 7, 10, 9, 0)),
                new ApprovalStepItem(2, 30L, "JP-MGR-002", "鈴木 一郎", ApprovalStatus.PENDING, null, null));
        when(repository.findAccessibleDetail(7L, 1L)).thenReturn(Optional.of(header));
        when(repository.listSteps(7L)).thenReturn(steps);

        var result = service.getDetail(1L, 7L);

        assertEquals(7L, result.approvalId());
        assertEquals("JP-EMP-001", result.applicantEmployeeCode());
        assertEquals(steps, result.steps());
    }

    @Test
    void inaccessibleDetailIsReportedAsNotFoundWithoutLoadingSteps() {
        ApprovalHistoryQueryRepository repository = Mockito.mock(ApprovalHistoryQueryRepository.class);
        ApprovalHistoryServiceImpl service = new ApprovalHistoryServiceImpl(repository);
        when(repository.findAccessibleDetail(7L, 99L)).thenReturn(Optional.empty());

        assertThrows(BizException.class, () -> service.getDetail(99L, 7L));
        verify(repository, Mockito.never()).listSteps(7L);
    }

    @Test
    void historyDelegatesViewerIdentityToQueryRepository() {
        ApprovalHistoryQueryRepository repository = Mockito.mock(ApprovalHistoryQueryRepository.class);
        ApprovalHistoryServiceImpl service = new ApprovalHistoryServiceImpl(repository);
        List<ApprovalHistoryItem> history = List.of(new ApprovalHistoryItem(
                7L, 99L, "PAID_LEAVE", 1L, "JP-EMP-001", "山田 太郎", ApprovalStatus.APPROVED,
                LocalDateTime.of(2026, 7, 9, 9, 0),
                LocalDateTime.of(2026, 7, 10, 9, 0)));
        when(repository.listHistory(20L)).thenReturn(history);

        assertEquals(history, service.listHistory(20L));
    }

    private ApprovalDetailHeader header() {
        return new ApprovalDetailHeader(
                7L, 99L, "PAID_LEAVE", 1L, "JP-EMP-001", "山田 太郎",
                2, 2, ApprovalStatus.PENDING,
                LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12), "leave",
                LocalDateTime.of(2026, 7, 9, 9, 0), null);
    }
}
