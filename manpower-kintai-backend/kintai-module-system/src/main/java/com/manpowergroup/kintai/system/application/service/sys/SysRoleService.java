package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.RoleCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

import java.util.List;

public interface SysRoleService  {

    /**
     * IDに対応するロールを取得する。
     *
     * @param id 対象のロールID
     * @return 取得したロール
     */
    SysRole getById(Long id);

    /**
     * 指定された検索条件でロールをページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param request 処理対象のページング条件
     * @return ページングされたロール一覧
     */
    PageResult<SysRole> page(Long companyId, PageRequest request);

    /**
     * 会社に一致するロールを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return ロール一覧。該当しない場合は空リスト
     */
    List<SysRole> listByCompany(Long companyId);

    /**
     * ロールを新規作成する。
     *
     * @param command 処理対象のロール作成コマンド
     * @return 保存したロール
     */
    SysRole create(RoleCreateCommand command);

    /**
     * ロールを更新する。
     *
     * @param id 対象のロールID
     * @param command 処理対象のロール更新コマンド
     * @return 更新したロール
     */
    SysRole update(Long id, RoleUpdateCommand command);

    /**
     * ロールを有効化する。
     *
     * @param id 対象のロールID
     */
    void enable(Long id);

    /**
     * ロールを無効化する。
     *
     * @param id 対象のロールID
     */
    void disable(Long id);

    /**
     * 指定IDのロールを削除する。
     *
     * @param id 対象のロールID
     */
    void remove(Long id);
}
