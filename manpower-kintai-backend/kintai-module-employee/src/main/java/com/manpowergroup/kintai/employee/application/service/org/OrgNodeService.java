package com.manpowergroup.kintai.employee.application.service.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.NodeUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;

import java.util.List;

public interface OrgNodeService {

    /**
     * IDに対応する組織ノードを取得する。
     *
     * @param id 対象の組織ノードID
     * @return 取得した組織ノード
     */
    OrgNode getById(Long id);

    /**
     * 指定された検索条件で組織ノードをページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param request 処理対象のページング条件
     * @return ページングされた組織ノード一覧
     */
    PageResult<OrgNode> pageByCompany(Long companyId, PageRequest request);

    /**
     * 指定された条件に一致する有効な組織ノードを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 組織ノード一覧。該当しない場合は空リスト
     */
    List<OrgNode> listEnabledByCompany(Long companyId);

    /**
     * 組織ノードを新規作成する。
     *
     * @param command 処理対象の組織ノード作成コマンド
     * @return 保存した組織ノード
     */
    OrgNode create(NodeCreateCommand command);

    /**
     * 組織ノードを更新する。
     *
     * @param id 対象の組織ノードID
     * @param command 処理対象の組織ノード更新コマンド
     * @return 更新した組織ノード
     */
    OrgNode update(Long id, NodeUpdateCommand command);

    /**
     * 組織ノードを有効化する。
     *
     * @param id 対象の組織ノードID
     */
    void enable(Long id);

    /**
     * 組織ノードを無効化する。
     *
     * @param id 対象の組織ノードID
     */
    void disable(Long id);

    /**
     * 指定IDの組織ノードを削除する。
     *
     * @param id 対象の組織ノードID
     */
    void remove(Long id);
}
