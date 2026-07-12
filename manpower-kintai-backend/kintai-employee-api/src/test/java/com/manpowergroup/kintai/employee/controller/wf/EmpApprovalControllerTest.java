package com.manpowergroup.kintai.employee.controller.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.request.ApprovalDecisionRequest;
import com.manpowergroup.kintai.attendance.application.dto.wf.request.ApprovalDelegateRequest;
import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalDecisionService;
import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalInboxService;
import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalHistoryService;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmpApprovalControllerTest {

    @Test
    void approveUsesAuthenticatedEmployeeAsApprover() {
        ApprovalDecisionService service = Mockito.mock(ApprovalDecisionService.class);
        EmpApprovalController controller = new EmpApprovalController(
                service, Mockito.mock(ApprovalInboxService.class),
                Mockito.mock(ApprovalHistoryService.class));

        controller.approve(new LoginPrincipal(20L, 200L), 7L,
                new ApprovalDecisionRequest("approved"));

        verify(service).approve(7L, 20L, "approved");
    }

    @Test
    void rejectUsesAuthenticatedEmployeeAsApprover() {
        ApprovalDecisionService service = Mockito.mock(ApprovalDecisionService.class);
        EmpApprovalController controller = new EmpApprovalController(
                service, Mockito.mock(ApprovalInboxService.class),
                Mockito.mock(ApprovalHistoryService.class));

        controller.reject(new LoginPrincipal(20L, 200L), 7L,
                new ApprovalDecisionRequest("rejected"));

        verify(service).reject(7L, 20L, "rejected");
    }

    @Test
    void pendingUsesAuthenticatedEmployeeAsApprover() {
        ApprovalDecisionService decisionService = Mockito.mock(ApprovalDecisionService.class);
        ApprovalInboxService inboxService = Mockito.mock(ApprovalInboxService.class);
        EmpApprovalController controller = new EmpApprovalController(
                decisionService, inboxService, Mockito.mock(ApprovalHistoryService.class));

        assertNotNull(controller.pending(new LoginPrincipal(20L, 200L)));

        verify(inboxService).listPending(20L);
    }

    @Test
    void delegateUsesAuthenticatedEmployeeAndRequestedTarget() {
        ApprovalDecisionService decisionService = Mockito.mock(ApprovalDecisionService.class);
        ApprovalInboxService inboxService = Mockito.mock(ApprovalInboxService.class);
        EmpApprovalController controller = new EmpApprovalController(
                decisionService, inboxService, Mockito.mock(ApprovalHistoryService.class));

        controller.delegate(new LoginPrincipal(20L, 200L), 7L,
                new ApprovalDelegateRequest(30L));

        verify(decisionService).delegate(7L, 20L, 30L);
    }

    @Test
    void detailAndHistoryUseAuthenticatedEmployeeAsViewer() {
        ApprovalDecisionService decisionService = Mockito.mock(ApprovalDecisionService.class);
        ApprovalInboxService inboxService = Mockito.mock(ApprovalInboxService.class);
        ApprovalHistoryService historyService = Mockito.mock(ApprovalHistoryService.class);
        EmpApprovalController controller = new EmpApprovalController(
                decisionService, inboxService, historyService);
        LoginPrincipal principal = new LoginPrincipal(20L, 200L);

        controller.detail(principal, 7L);
        controller.history(principal);

        verify(historyService).getDetail(20L, 7L);
        verify(historyService).listHistory(20L);
    }
}
