package com.manpowergroup.kintai.attendance.application.service.wf;

import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleUpdateCommand;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;

import java.util.List;

public interface WfApprovalRuleService {

    WfApprovalRule getById(Long ruleId);

    List<WfApprovalRule> listByCompany(Long companyId);

    WfApprovalRule create(ApprovalRuleCreateCommand command);

    WfApprovalRule update(Long ruleId, ApprovalRuleUpdateCommand command);

    void enable(Long ruleId);

    void disable(Long ruleId);

    void remove(Long ruleId);
}
