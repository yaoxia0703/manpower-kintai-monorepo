package com.manpowergroup.kintai.attendance.infrastructure.mapper.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalDetailHeader;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalHistoryItem;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalStepItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApprovalHistoryQueryMapper {

    ApprovalDetailHeader selectAccessibleDetail(
            @Param("approvalId") Long approvalId,
            @Param("viewerId") Long viewerId);

    List<ApprovalStepItem> selectSteps(@Param("approvalId") Long approvalId);

    List<ApprovalHistoryItem> selectHistory(@Param("viewerId") Long viewerId);
}
