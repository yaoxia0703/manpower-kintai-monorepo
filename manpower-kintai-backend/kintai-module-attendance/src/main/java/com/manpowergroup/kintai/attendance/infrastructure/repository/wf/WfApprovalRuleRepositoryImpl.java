package com.manpowergroup.kintai.attendance.infrastructure.repository.wf;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRuleRepository;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.wf.WfApprovalRuleMapper;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WfApprovalRuleRepositoryImpl implements WfApprovalRuleRepository {

    private final WfApprovalRuleMapper mapper;

    @Override
    public Optional<WfApprovalRule> findApplicable(
            Long companyId, String requestType, BigDecimal amount) {
        return mapper.selectList(Wrappers.<WfApprovalRule>lambdaQuery()
                        .eq(WfApprovalRule::getCompanyId, companyId)
                        .eq(WfApprovalRule::getRequestType, requestType)
                        .eq(WfApprovalRule::getStatus, Status.ENABLED)
                        .orderByDesc(WfApprovalRule::getAmountThreshold)
                        .orderByAsc(WfApprovalRule::getSort))
                .stream()
                .filter(rule -> rule.appliesTo(requestType, amount))
                .findFirst();
    }

    @Override
    public Optional<WfApprovalRule> findById(Long ruleId) {
        return Optional.ofNullable(mapper.selectById(ruleId));
    }

    @Override
    public List<WfApprovalRule> listByCompany(Long companyId) {
        return mapper.selectList(Wrappers.<WfApprovalRule>lambdaQuery()
                .eq(WfApprovalRule::getCompanyId, companyId)
                .orderByAsc(WfApprovalRule::getSort)
                .orderByAsc(WfApprovalRule::getId));
    }

    @Override
    public WfApprovalRule save(WfApprovalRule rule) {
        mapper.insert(rule);
        return rule;
    }

    @Override
    public WfApprovalRule update(WfApprovalRule rule) {
        mapper.updateById(rule);
        return rule;
    }

    @Override
    public void deleteById(Long ruleId) {
        mapper.deleteById(ruleId);
    }
}
