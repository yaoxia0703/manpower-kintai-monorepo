package com.manpowergroup.kintai.attendance.infrastructure.repository.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailHeader;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalStepItem;
import com.manpowergroup.kintai.attendance.application.query.wf.ApprovalHistoryQueryRepository;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.wf.ApprovalHistoryQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApprovalHistoryQueryRepositoryImpl implements ApprovalHistoryQueryRepository {

    private final ApprovalHistoryQueryMapper mapper;

    @Override
    public Optional<ApprovalDetailHeader> findAccessibleDetail(Long approvalId, Long viewerId) {
        return Optional.ofNullable(mapper.selectAccessibleDetail(approvalId, viewerId));
    }

    @Override
    public List<ApprovalStepItem> listSteps(Long approvalId) {
        return mapper.selectSteps(approvalId);
    }

    @Override
    public List<ApprovalHistoryItem> listHistory(Long viewerId) {
        return mapper.selectHistory(viewerId);
    }
}
