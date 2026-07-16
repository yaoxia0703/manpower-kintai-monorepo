package com.manpowergroup.kintai.attendance.application.service.impl.wf;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleUpdateCommand;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRuleRepository;
import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WfApprovalRuleServiceImplTest {

    @Test
    void createBuildsRuleThroughDomainFactoryAndSavesIt() {
        WfApprovalRuleRepository repository = Mockito.mock(WfApprovalRuleRepository.class);
        WfApprovalRuleServiceImpl service = new WfApprovalRuleServiceImpl(repository);

        WfApprovalRule result = service.create(new ApprovalRuleCreateCommand(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.DIRECT_ONLY,
                null, null, null, 1, null));

        assertEquals(Status.ENABLED, result.getStatus());
        verify(repository).save(result);
    }

    @Test
    void updatePreservesCompanyAndStatusWhileUsingDomainBehavior() {
        WfApprovalRuleRepository repository = Mockito.mock(WfApprovalRuleRepository.class);
        WfApprovalRuleServiceImpl service = new WfApprovalRuleServiceImpl(repository);
        WfApprovalRule existing = WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.DIRECT_ONLY,
                null, null, null, 1, Status.DISABLED).setId(7L);
        when(repository.findById(7L)).thenReturn(Optional.of(existing));

        WfApprovalRule result = service.update(7L, new ApprovalRuleUpdateCommand(
                RequestType.PAID_LEAVE, ApprovalStopCondition.REACH_GRADE, "L4", null, null, 2));

        assertEquals(10L, result.getCompanyId());
        assertEquals(Status.DISABLED, result.getStatus());
        assertEquals(ApprovalStopCondition.REACH_GRADE, result.getStopCondition());
        verify(repository).update(existing);
    }

    @Test
    void enableDisableRemoveAndListDelegateThroughRepository() {
        WfApprovalRuleRepository repository = Mockito.mock(WfApprovalRuleRepository.class);
        WfApprovalRuleServiceImpl service = new WfApprovalRuleServiceImpl(repository);
        WfApprovalRule existing = WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.DIRECT_ONLY,
                null, null, null, 1, Status.ENABLED).setId(7L);
        when(repository.findById(7L)).thenReturn(Optional.of(existing));
        when(repository.listByCompany(10L)).thenReturn(List.of(existing));

        assertEquals(List.of(existing), service.listByCompany(10L));
        service.disable(7L);
        assertEquals(Status.DISABLED, existing.getStatus());
        service.enable(7L);
        assertEquals(Status.ENABLED, existing.getStatus());
        service.remove(7L);

        verify(repository, Mockito.times(2)).update(existing);
        verify(repository).deleteById(7L);
    }
}
