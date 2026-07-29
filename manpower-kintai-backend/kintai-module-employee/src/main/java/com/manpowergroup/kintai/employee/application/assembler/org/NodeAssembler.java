package com.manpowergroup.kintai.employee.application.assembler.org;

import com.manpowergroup.kintai.employee.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.NodeUpdateCommand;
import com.manpowergroup.kintai.employee.application.dto.org.response.NodeResponse;
import com.manpowergroup.kintai.employee.application.dto.org.request.NodeCreateRequest;
import com.manpowergroup.kintai.employee.application.dto.org.request.NodeUpdateRequest;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;

public final class NodeAssembler {

    private NodeAssembler() {
    }

    /**
     * 入力データを組織ノード作成コマンドへ変換する。
     *
     * @param request 変換対象の組織ノード作成リクエスト
     * @return 変換後の組織ノード作成コマンド
     */
    public static NodeCreateCommand toCommand(NodeCreateRequest request) {
        return new NodeCreateCommand(
                request.getCompanyId(), request.getParentId(), request.getManagerId(), request.getName(),
                request.getTypeCode(), request.getDeptFunction(), request.getCode(), request.getLevel(),
                request.getSort(), request.getStatus()
        );
    }

    /**
     * 入力データを組織ノード更新コマンドへ変換する。
     *
     * @param request 変換対象の組織ノード更新リクエスト
     * @return 変換後の組織ノード更新コマンド
     */
    public static NodeUpdateCommand toCommand(NodeUpdateRequest request) {
        return new NodeUpdateCommand(
                request.getCompanyId(), request.getParentId(), request.getManagerId(), request.getName(),
                request.getTypeCode(), request.getDeptFunction(), request.getCode(), request.getLevel(),
                request.getSort(), request.getStatus()
        );
    }

    /**
     * 入力データを組織ノードレスポンスへ変換する。
     *
     * @param node 処理対象の組織ノード
     * @return 変換後の組織ノードレスポンス
     */
    public static NodeResponse toResponse(OrgNode node) {
        return NodeResponse.from(node);
    }
}
