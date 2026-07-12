package com.manpowergroup.kintai.system.application.command.emp;

public record AccountCreateCommand(
        // 社員ID
        Long employeeId,

        // ログインユーザー名
        String username,

        // 初期パスワード
        String password
) {
}
