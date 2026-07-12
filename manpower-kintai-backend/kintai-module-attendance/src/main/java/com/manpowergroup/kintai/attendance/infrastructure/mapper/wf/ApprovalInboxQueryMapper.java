package com.manpowergroup.kintai.attendance.infrastructure.mapper.wf;

import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalInboxItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApprovalInboxQueryMapper {

    List<ApprovalInboxItem> selectCurrentPendingByApprover(
            @Param("approverId") Long approverId);
}
