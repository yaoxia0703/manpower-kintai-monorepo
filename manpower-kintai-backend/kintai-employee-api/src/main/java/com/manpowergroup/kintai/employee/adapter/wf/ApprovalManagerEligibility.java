package com.manpowergroup.kintai.employee.adapter.wf;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.application.service.sys.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalManagerEligibility {

    private static final String APPROVAL_READ_PERMISSION = "manager:approval:read";

    private final SysPermissionService permissionService;

    /**
     * 対象社員が指定申請を承認できるか判定する。
     *
     * @param employeeId 対象の社員ID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    public boolean canApprove(Long employeeId) {
        return permissionService.listByEmployeeId(employeeId).stream()
                .anyMatch(permission -> permission.getStatus() == Status.ENABLED
                        && APPROVAL_READ_PERMISSION.equals(permission.getCode()));
    }
}
