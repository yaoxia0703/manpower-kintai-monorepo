package com.manpowergroup.kintai.employee.application.command.org;

import com.manpowergroup.kintai.common.enums.Status;

public record CompanyUpdateCommand(
        // 親会社ID
        Long parentId,

        // 会社名
        String name,

        // 会社コード
        String companyCode,

        // 階層レベル
        Integer level,

        // 表示順
        Integer sort,

        // ステータス
        Status status
) {
}
