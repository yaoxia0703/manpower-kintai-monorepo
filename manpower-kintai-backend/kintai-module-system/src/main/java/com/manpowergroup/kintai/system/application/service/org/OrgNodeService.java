package com.manpowergroup.kintai.system.application.service.org;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.system.application.command.org.NodeUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;

import java.util.List;

// 組織ノードサービス（アプリケーション層）
public interface OrgNodeService extends IService<OrgNode> {

    // IDで組織ノードを取得（存在しない場合は例外）
    OrgNode getById(Long id);

    // 会社IDで組織ノード一覧をページング取得
    PageResult<OrgNode> pageByCompany(Long companyId, PageRequest request);

    // 会社IDで有効な全ノードを取得（ツリー構築用）
    List<OrgNode> listEnabledByCompany(Long companyId);

    // 組織ノードを新規作成（Closure Tableも同時更新）
    OrgNode create(NodeCreateCommand command);

    // 組織ノードを更新
    OrgNode update(Long id, NodeUpdateCommand command);

    // 組織ノードを有効化
    void enable(Long id);

    // 組織ノードを無効化
    void disable(Long id);

    // 組織ノードを削除（論理削除 + Closure Table削除）
    void remove(Long id);
}

