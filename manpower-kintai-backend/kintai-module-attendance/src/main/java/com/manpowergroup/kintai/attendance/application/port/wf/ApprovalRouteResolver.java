package com.manpowergroup.kintai.attendance.application.port.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;

import java.util.List;

public interface ApprovalRouteResolver {

    List<Long> resolveApprovers(Long employeeId, Long companyId, WfApprovalRule rule);
}
