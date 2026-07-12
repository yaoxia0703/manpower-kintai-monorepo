package com.manpowergroup.kintai.system.application.command.org;

import com.manpowergroup.kintai.common.enums.Status;

public record NodeUpdateCommand(
        // 会社ID
        Long companyId,

        // 親ノードID
        Long parentId,

        // ノード責任者社員ID
        Long managerId,

        // ノード名
        String name,

        // ノード種別コード
        String typeCode,

        // 部署機能
        String deptFunction,

        // ノードコード
        String code,

        // 階層レベル
        Integer level,

        // 表示順
        Integer sort,

        // ステータス
        Status status
) {
}
