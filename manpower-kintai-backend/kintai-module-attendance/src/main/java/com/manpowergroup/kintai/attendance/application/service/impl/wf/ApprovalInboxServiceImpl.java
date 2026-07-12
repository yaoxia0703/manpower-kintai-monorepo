package com.manpowergroup.kintai.attendance.application.service.impl.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;
import com.manpowergroup.kintai.attendance.application.query.wf.ApprovalInboxQueryRepository;
import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalInboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalInboxServiceImpl implements ApprovalInboxService {

    private final ApprovalInboxQueryRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalInboxItem> listPending(Long approverId) {
        return repository.listCurrentPendingByApprover(approverId);
    }
}
