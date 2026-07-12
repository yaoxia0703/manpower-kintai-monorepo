package com.manpowergroup.kintai.admin.controller.wf;

import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleCreateCommand;
import com.manpowergroup.kintai.attendance.application.dto.wf.request.ApprovalRuleCreateRequest;
import com.manpowergroup.kintai.attendance.application.service.wf.WfApprovalRuleService;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class AdminApprovalRuleControllerTest {

    @Test
    void createMapsValidatedRequestToApplicationCommand() {
        WfApprovalRuleService service = Mockito.mock(WfApprovalRuleService.class);
        AdminApprovalRuleController controller = new AdminApprovalRuleController(service);
        ApprovalRuleCreateRequest request = new ApprovalRuleCreateRequest(
                10L, "PAID_LEAVE", "DIRECT_ONLY",
                null, null, null, 1, null);
        when(service.create(any(ApprovalRuleCreateCommand.class))).thenReturn(
                WfApprovalRule.create(
                        10L, "PAID_LEAVE", "DIRECT_ONLY",
                        null, null, null, 1, Status.ENABLED));

        controller.create(request);

        ArgumentCaptor<ApprovalRuleCreateCommand> captor =
                ArgumentCaptor.forClass(ApprovalRuleCreateCommand.class);
        verify(service).create(captor.capture());
        assertEquals(10L, captor.getValue().companyId());
        assertEquals("PAID_LEAVE", captor.getValue().requestType());
    }

    @Test
    void enableDelegatesRuleIdentityToApplicationService() {
        WfApprovalRuleService service = Mockito.mock(WfApprovalRuleService.class);
        AdminApprovalRuleController controller = new AdminApprovalRuleController(service);

        controller.enable(7L);

        verify(service).enable(7L);
    }
}
