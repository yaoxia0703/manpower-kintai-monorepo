package com.manpowergroup.kintai.employee.domain.repository.org;

import com.manpowergroup.kintai.employee.domain.entity.org.OrgNodeClosure;

import java.util.List;

// 組織閉包テーブルリポジトリ（ドメイン層インターフェース）
public interface OrgNodeClosureRepository {

    // 指定ノードの全子孫を取得
    List<OrgNodeClosure> findDescendants(Long ancestorId);

    // 指定ノードの全祖先を取得
    List<OrgNodeClosure> findAncestors(Long descendantId);

    // Closureレコードを一括保存
    void saveBatch(List<OrgNodeClosure> closures);

    // 指定ノードIDに関連する全Closureを削除
    void deleteByDescendantId(Long descendantId);

    void deleteExternalAncestorLinks(List<Long> subtreeNodeIds);
}


