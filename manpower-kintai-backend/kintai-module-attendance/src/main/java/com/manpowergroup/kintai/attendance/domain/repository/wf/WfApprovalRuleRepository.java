package com.manpowergroup.kintai.attendance.domain.repository.wf;

import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;
import com.manpowergroup.kintai.attendance.domain.enums.RequestType;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

public interface WfApprovalRuleRepository {

    /**
     * 申請条件に適用される承認ルールを取得する。
     *
     * @param companyId 対象の会社ID
     * @param requestType 申請種別
     * @param amount 申請金額
     * @return 対象が存在する場合は承認ルールを含むOptional、存在しない場合はOptional.empty()
     */
    Optional<WfApprovalRule> findApplicable(Long companyId, RequestType requestType, BigDecimal amount);

    /**
     * IDに対応する承認ルールを取得する。
     *
     * @param ruleId 対象の承認ルールID
     * @return 対象が存在する場合は承認ルールを含むOptional、存在しない場合はOptional.empty()
     */
    Optional<WfApprovalRule> findById(Long ruleId);

    /**
     * 会社に一致する承認ルールを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 承認ルール一覧。該当しない場合は空リスト
     */
    List<WfApprovalRule> listByCompany(Long companyId);

    /**
     * 承認ルールを保存する。
     *
     * @param rule 対象の承認ルール
     * @return 保存した承認ルール
     */
    WfApprovalRule save(WfApprovalRule rule);

    /**
     * 承認ルールを更新する。
     *
     * @param rule 対象の承認ルール
     * @return 更新した承認ルール
     */
    WfApprovalRule update(WfApprovalRule rule);

    /**
     * 指定IDの承認ルールを削除する。
     *
     * @param ruleId 対象の承認ルールID
     */
    void deleteById(Long ruleId);
}
