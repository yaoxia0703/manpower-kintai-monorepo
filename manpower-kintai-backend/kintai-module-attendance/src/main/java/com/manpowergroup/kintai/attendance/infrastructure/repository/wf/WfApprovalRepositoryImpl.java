package com.manpowergroup.kintai.attendance.infrastructure.repository.wf;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApproval;
import com.manpowergroup.kintai.attendance.domain.repository.wf.WfApprovalRepository;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.wf.WfApprovalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WfApprovalRepositoryImpl implements WfApprovalRepository {

    private final WfApprovalMapper mapper;

    @Override
    public Optional<WfApproval> findByIdForUpdate(Long approvalId) {
        return Optional.ofNullable(mapper.selectOne(Wrappers.<WfApproval>lambdaQuery()
                .eq(WfApproval::getId, approvalId)
                .last("FOR UPDATE")));
    }

    @Override
    public Optional<WfApproval> findByRequestIdForUpdate(Long requestId) {
        return Optional.ofNullable(mapper.selectOne(Wrappers.<WfApproval>lambdaQuery()
                .eq(WfApproval::getRequestId, requestId)
                .last("FOR UPDATE")));
    }

    @Override
    public WfApproval save(WfApproval approval) {
        mapper.insert(approval);
        return approval;
    }

    @Override
    public WfApproval update(WfApproval approval) {
        mapper.updateById(approval);
        return approval;
    }
}
