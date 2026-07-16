package com.manpowergroup.kintai.attendance.domain.entity.wf;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStopCondition;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WfApprovalRuleTest {

    @Test
    void createDefaultsStatusToEnabledWhenStatusIsNotSpecified() {
        WfApprovalRule rule = WfApprovalRule.create(
                10L,
                RequestType.PAID_LEAVE,
                ApprovalStopCondition.DIRECT_ONLY,
                null,
                null,
                null,
                1,
                null);

        assertEquals(10L, rule.getCompanyId());
        assertEquals(RequestType.PAID_LEAVE, rule.getRequestType());
        assertEquals(ApprovalStopCondition.DIRECT_ONLY, rule.getStopCondition());
        assertEquals(1, rule.getSort());
        assertEquals(Status.ENABLED, rule.getStatus());
    }

    @Test
    void enabledRuleWithoutThresholdAppliesToMatchingRequestType() {
        WfApprovalRule rule = WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.DIRECT_ONLY, null, null, null, 1, Status.ENABLED);

        assertTrue(rule.appliesTo(RequestType.PAID_LEAVE, null));
        assertFalse(rule.appliesTo(RequestType.OVERTIME, null));
    }

    @Test
    void thresholdRuleAppliesWhenAmountIsAtOrAboveThreshold() {
        WfApprovalRule rule = WfApprovalRule.create(
                10L, RequestType.OVERTIME, ApprovalStopCondition.DIRECT_ONLY, null, null,
                BigDecimal.valueOf(1000), 1, Status.ENABLED);

        assertFalse(rule.appliesTo(RequestType.OVERTIME, BigDecimal.valueOf(999)));
        assertTrue(rule.appliesTo(RequestType.OVERTIME, BigDecimal.valueOf(1000)));
        assertTrue(rule.appliesTo(RequestType.OVERTIME, BigDecimal.valueOf(1500)));
    }

    @Test
    void disabledRuleDoesNotApply() {
        WfApprovalRule rule = WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.DIRECT_ONLY, null, null, null, 1, Status.ENABLED);

        rule.disable();

        assertFalse(rule.appliesTo(RequestType.PAID_LEAVE, null));
    }

    @Test
    void updateRuleFieldsKeepsIdentityAndStatus() {
        WfApprovalRule rule = WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.DIRECT_ONLY, null, null, null, 1, Status.DISABLED)
                .setId(99L);

        rule.updateRule(RequestType.OVERTIME, ApprovalStopCondition.REACH_GRADE, "L4", null,
                BigDecimal.valueOf(1000), 2);

        assertEquals(99L, rule.getId());
        assertEquals(10L, rule.getCompanyId());
        assertEquals(Status.DISABLED, rule.getStatus());
        assertEquals(RequestType.OVERTIME, rule.getRequestType());
        assertEquals(ApprovalStopCondition.REACH_GRADE, rule.getStopCondition());
        assertEquals("L4", rule.getStopGradeLevel());
        assertEquals(BigDecimal.valueOf(1000), rule.getAmountThreshold());
        assertEquals(2, rule.getSort());
    }

    @Test
    void routeStopConditionRequiresSupportedConfiguration() {
        assertThrows(BizException.class, () -> WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.REACH_GRADE,
                null, null, null, 1, Status.ENABLED));
        assertThrows(BizException.class, () -> WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.REACH_DEPARTMENT,
                null, null, null, 1, Status.ENABLED));
        assertThrows(BizException.class, () -> WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, null,
                null, null, null, 1, Status.ENABLED));
    }

    @Test
    void ruleRejectsNegativeThresholdAndSort() {
        assertThrows(BizException.class, () -> WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.DIRECT_ONLY,
                null, null, BigDecimal.valueOf(-1), 1, Status.ENABLED));
        assertThrows(BizException.class, () -> WfApprovalRule.create(
                10L, RequestType.PAID_LEAVE, ApprovalStopCondition.DIRECT_ONLY,
                null, null, null, -1, Status.ENABLED));
    }
}
