package com.manpowergroup.kintai.system.application.service.sys;

import com.manpowergroup.kintai.system.application.dto.sys.response.RoleSummary;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Public application boundary for business modules that need RBAC role data.
 */
public interface RoleAccessService {

    /**
     * 指定された条件に一致する有効なロールアクセス情報を一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return ロールSummary一覧。該当しない場合は空リスト
     */
    List<RoleSummary> listEnabledByCompany(Long companyId);

    /**
     * 指定した全ロールが対象会社で有効か判定する。
     *
     * @param companyId 対象の会社ID
     * @param roleIds 対象のロールID一覧
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean areAllEnabledForCompany(Long companyId, Collection<Long> roleIds);

    /**
     * 指定社員に有効なロールが割り当てられているか判定する。
     *
     * @param employeeId 対象の社員ID
     * @param roleCode ロールコード
     * @param effectiveDate 適用基準日
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean employeeHasActiveRole(Long employeeId, String roleCode, LocalDate effectiveDate);
}
