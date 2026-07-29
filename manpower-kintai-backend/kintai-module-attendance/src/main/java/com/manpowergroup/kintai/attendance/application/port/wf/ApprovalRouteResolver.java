package com.manpowergroup.kintai.attendance.application.port.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;

import java.util.List;

public interface ApprovalRouteResolver {

    /**
     * 申請内容に基づいて承認者一覧を解決する。
     *
     * @param employeeId 対象の社員ID
     * @param companyId 対象の会社ID
     * @param rule 対象の承認ルール
     * @return 承認者の社員ID一覧。該当しない場合は空リスト
     */
    List<Long> resolveApprovers(Long employeeId, Long companyId, WfApprovalRule rule);
}
