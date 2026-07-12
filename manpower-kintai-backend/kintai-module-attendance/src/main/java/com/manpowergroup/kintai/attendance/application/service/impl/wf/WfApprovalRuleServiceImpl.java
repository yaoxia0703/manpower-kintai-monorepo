package com.manpowergroup.kintai.attendance.application.service.impl.wf;

import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleUpdateCommand;
import com.manpowergroup.kintai.attendance.application.service.wf.WfApprovalRuleService;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRuleRepository;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WfApprovalRuleServiceImpl implements WfApprovalRuleService {

    private final WfApprovalRuleRepository repository;

    @Override
    public WfApprovalRule getById(Long ruleId) {
        return requireRule(ruleId);
    }

    @Override
    public List<WfApprovalRule> listByCompany(Long companyId) {
        return repository.listByCompany(companyId);
    }

    @Override
    @Transactional
    public WfApprovalRule create(ApprovalRuleCreateCommand command) {
        WfApprovalRule rule = WfApprovalRule.create(
                command.companyId(), command.requestType(), command.stopCondition(),
                command.stopGradeLevel(), command.stopDeptFunc(),
                command.amountThreshold(), command.sort(), command.status());
        repository.save(rule);
        return rule;
    }

    @Override
    @Transactional
    public WfApprovalRule update(Long ruleId, ApprovalRuleUpdateCommand command) {
        WfApprovalRule rule = requireRule(ruleId);
        rule.updateRule(
                command.requestType(), command.stopCondition(), command.stopGradeLevel(),
                command.stopDeptFunc(), command.amountThreshold(), command.sort());
        repository.update(rule);
        return rule;
    }

    @Override
    @Transactional
    public void enable(Long ruleId) {
        WfApprovalRule rule = requireRule(ruleId);
        rule.enable();
        repository.update(rule);
    }

    @Override
    @Transactional
    public void disable(Long ruleId) {
        WfApprovalRule rule = requireRule(ruleId);
        rule.disable();
        repository.update(rule);
    }

    @Override
    @Transactional
    public void remove(Long ruleId) {
        requireRule(ruleId);
        repository.deleteById(ruleId);
    }

    private WfApprovalRule requireRule(Long ruleId) {
        return repository.findById(ruleId)
                .orElseThrow(() -> BizException.withDetail(
                        ErrorCode.NOT_FOUND, "approval rule not found"));
    }
}
