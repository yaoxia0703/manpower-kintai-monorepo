package com.manpowergroup.kintai.employee.application.service.emp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.employee.application.command.emp.AccountCreateCommand;
import com.manpowergroup.kintai.employee.application.command.emp.AccountUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import org.springframework.transaction.annotation.Transactional;

public interface EmpAccountService {

    /**
     * IDに対応する社員アカウントを取得する。
     *
     * @param id 対象の社員アカウントID
     * @return 取得した社員アカウント
     */
    EmpAccount getById(Long id);

    /**
     * 社員IDに対応する社員アカウントを取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 取得した社員アカウント
     */
    EmpAccount getByEmployeeId(Long employeeId);

    /**
     * 社員アカウントを新規作成する。
     *
     * @param command 処理対象の社員アカウント作成コマンド
     * @return 保存した社員アカウント
     */
    EmpAccount create(AccountCreateCommand command);

    /**
     * 社員アカウントを更新する。
     *
     * @param id 対象の社員アカウントID
     * @param command 処理対象の社員アカウント更新コマンド
     * @return 更新した社員アカウント
     */
    EmpAccount update(Long id, AccountUpdateCommand command);

    /**
     * 社員アカウントの設定を変更する。
     *
     * @param id 対象の社員アカウントID
     * @param oldPassword 現在のパスワード
     * @param newPassword 変更後のパスワード
     */
    void changePassword(Long id, String oldPassword, String newPassword);

    /**
     * 社員アカウントを有効化する。
     *
     * @param id 対象の社員アカウントID
     */
    void enable(Long id);

    /**
     * 社員アカウントを無効化する。
     *
     * @param id 対象の社員アカウントID
     */
    void disable(Long id);

    /**
     * 指定IDの社員アカウントを削除する。
     *
     * @param id 対象の社員アカウントID
     */
    void remove(Long id);
}
