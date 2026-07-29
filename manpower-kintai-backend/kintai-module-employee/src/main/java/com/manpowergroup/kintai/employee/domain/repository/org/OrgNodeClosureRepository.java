package com.manpowergroup.kintai.employee.domain.repository.org;

import com.manpowergroup.kintai.employee.domain.entity.org.OrgNodeClosure;

import java.util.List;

// 組織閉包テーブルリポジトリ（ドメイン層インターフェース）
public interface OrgNodeClosureRepository {

    /**
     * 指定組織ノードの子孫ノードを取得する。
     *
     * @param ancestorId 祖先ノードID
     * @return 組織階層関係一覧。該当しない場合は空リスト
     */
    List<OrgNodeClosure> findDescendants(Long ancestorId);

    /**
     * 指定組織ノードの祖先ノードを取得する。
     *
     * @param descendantId 子孫ノードID
     * @return 組織階層関係一覧。該当しない場合は空リスト
     */
    List<OrgNodeClosure> findAncestors(Long descendantId);

    /**
     * 組織階層関係を保存する。
     *
     * @param closures 保存対象の組織階層関係一覧
     */
    void saveBatch(List<OrgNodeClosure> closures);

    /**
     * 指定組織ノードを子孫とする階層関係を削除する。
     *
     * @param descendantId 子孫ノードID
     */
    void deleteByDescendantId(Long descendantId);

    /**
     * 移動対象サブツリーに対する外部祖先リンクを削除する。
     *
     * @param subtreeNodeIds 移動対象サブツリーの組織ノードID一覧
     */
    void deleteExternalAncestorLinks(List<Long> subtreeNodeIds);
}


