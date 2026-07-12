package com.manpowergroup.kintai.attendance.infrastructure.repository.wf;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalStep;
import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalStepRepository;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.wf.WfApprovalStepMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WfApprovalStepRepositoryImpl implements WfApprovalStepRepository {

    private final WfApprovalStepMapper mapper;

    @Override
    public Optional<WfApprovalStep> findByApprovalAndStep(Long approvalId, Integer step) {
        return Optional.ofNullable(mapper.selectOne(Wrappers.<WfApprovalStep>lambdaQuery()
                .eq(WfApprovalStep::getApprovalId, approvalId)
                .eq(WfApprovalStep::getStep, step)));
    }

    @Override
    public List<WfApprovalStep> listPendingByApproval(Long approvalId) {
        return mapper.selectList(Wrappers.<WfApprovalStep>lambdaQuery()
                .eq(WfApprovalStep::getApprovalId, approvalId)
                .eq(WfApprovalStep::getStatus, ApprovalStatus.PENDING)
                .orderByAsc(WfApprovalStep::getStep));
    }

    @Override
    public WfApprovalStep save(WfApprovalStep step) {
        mapper.insert(step);
        return step;
    }

    @Override
    public WfApprovalStep update(WfApprovalStep step) {
        mapper.updateById(step);
        return step;
    }
}
