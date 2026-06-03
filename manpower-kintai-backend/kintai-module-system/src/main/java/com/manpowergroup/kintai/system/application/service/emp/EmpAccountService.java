package com.manpowergroup.kintai.system.application.service.emp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.system.application.command.emp.AccountUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpAccount;

// 社員アカウントサービス（アプリケーション層）
public interface EmpAccountService extends IService<EmpAccount> {

    // IDでアカウントを取得（存在しない場合は例外）
    EmpAccount getById(Long id);

    // 社員IDでアカウントを取得
    EmpAccount getByEmployeeId(Long employeeId);

    // アカウントを新規作成（パスワードはBCryptでハッシュ化）
    EmpAccount create(AccountCreateCommand command);

    // アカウント情報を更新（ユーザー名等）
    EmpAccount update(Long id, AccountUpdateCommand command);

    // パスワードを変更
    void changePassword(Long id, String oldPassword, String newPassword);

    // アカウントを有効化
    void enable(Long id);

    // アカウントを無効化（ログイン停止）
    void disable(Long id);

    // アカウントを削除（論理削除）
    void remove(Long id);
}

