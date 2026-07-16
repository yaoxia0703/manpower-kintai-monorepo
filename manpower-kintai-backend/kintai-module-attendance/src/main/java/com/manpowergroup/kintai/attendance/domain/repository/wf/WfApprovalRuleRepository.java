package com.manpowergroup.kintai.attendance.domain.repository.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

public interface WfApprovalRuleRepository {

    Optional<WfApprovalRule> findApplicable(Long companyId, RequestType requestType, BigDecimal amount);

    Optional<WfApprovalRule> findById(Long ruleId);

    List<WfApprovalRule> listByCompany(Long companyId);

    WfApprovalRule save(WfApprovalRule rule);

    WfApprovalRule update(WfApprovalRule rule);

    void deleteById(Long ruleId);
}
