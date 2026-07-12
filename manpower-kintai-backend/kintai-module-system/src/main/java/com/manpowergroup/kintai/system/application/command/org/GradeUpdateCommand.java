package com.manpowergroup.kintai.system.application.command.org;

import com.manpowergroup.kintai.common.enums.Status;

public record GradeUpdateCommand(
        // 会社ID
        Long companyId,

        // 等級名
        String name,

        // 等級コード
        String code,

        // 等級レベル
        String gradeLevel,

        // 表示順
        Integer sort,

        // ステータス
        Status status
) {
}
