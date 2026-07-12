package com.manpowergroup.kintai.attendance.infrastructure.repository.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;
import com.manpowergroup.kintai.attendance.application.query.wf.ApprovalInboxQueryRepository;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.wf.ApprovalInboxQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApprovalInboxQueryRepositoryImpl implements ApprovalInboxQueryRepository {

    private final ApprovalInboxQueryMapper mapper;

    @Override
    public List<ApprovalInboxItem> listCurrentPendingByApprover(Long approverId) {
        return mapper.selectCurrentPendingByApprover(approverId);
    }
}
