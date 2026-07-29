package com.manpowergroup.kintai.attendance.application.service.wf;

import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleUpdateCommand;
import com.manpowergroup.kintai.attendance.domain.entity.wf.WfApprovalRule;

import java.util.List;

public interface WfApprovalRuleService {

    /**
     * IDに対応する承認ルールを取得する。
     *
     * @param ruleId 対象の承認ルールID
     * @return 取得した承認ルール
     */
    WfApprovalRule getById(Long ruleId);

    /**
     * 会社に一致する承認ルールを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 承認ルール一覧。該当しない場合は空リスト
     */
    List<WfApprovalRule> listByCompany(Long companyId);

    /**
     * 承認ルールを新規作成する。
     *
     * @param command 処理対象の承認ルール作成コマンド
     * @return 保存した承認ルール
     */
    WfApprovalRule create(ApprovalRuleCreateCommand command);

    /**
     * 承認ルールを更新する。
     *
     * @param ruleId 対象の承認ルールID
     * @param command 処理対象の承認ルール更新コマンド
     * @return 更新した承認ルール
     */
    WfApprovalRule update(Long ruleId, ApprovalRuleUpdateCommand command);

    /**
     * 承認ルールを有効化する。
     *
     * @param ruleId 対象の承認ルールID
     */
    void enable(Long ruleId);

    /**
     * 承認ルールを無効化する。
     *
     * @param ruleId 対象の承認ルールID
     */
    void disable(Long ruleId);

    /**
     * 指定IDの承認ルールを削除する。
     *
     * @param ruleId 対象の承認ルールID
     */
    void remove(Long ruleId);
}
