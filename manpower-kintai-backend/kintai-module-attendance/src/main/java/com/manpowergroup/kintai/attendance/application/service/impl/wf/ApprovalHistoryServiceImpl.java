package com.manpowergroup.kintai.attendance.application.service.impl.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailHeader;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailResponse;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;
import com.manpowergroup.kintai.attendance.application.query.wf.ApprovalHistoryQueryRepository;
import com.manpowergroup.kintai.attendance.application.service.wf.ApprovalHistoryService;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalHistoryServiceImpl implements ApprovalHistoryService {

    private final ApprovalHistoryQueryRepository repository;

    @Override
    @Transactional(readOnly = true)
    public ApprovalDetailResponse getDetail(Long viewerId, Long approvalId) {
        ApprovalDetailHeader header = repository.findAccessibleDetail(approvalId, viewerId)
                .orElseThrow(() -> BizException.withDetail(
                        ErrorCode.NOT_FOUND, "approval not found"));
        return ApprovalDetailResponse.from(header, repository.listSteps(approvalId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalHistoryItem> listHistory(Long viewerId) {
        return repository.listHistory(viewerId);
    }
}
