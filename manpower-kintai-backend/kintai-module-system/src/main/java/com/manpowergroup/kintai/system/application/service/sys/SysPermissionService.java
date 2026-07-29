package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.sys.PermissionCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.PermissionUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;

import java.util.List;

// 権限マスタサービス（アプリケーション層）
public interface SysPermissionService  {

    /**
     * IDに対応する権限を取得する。
     *
     * @param id 対象の権限ID
     * @return 取得した権限
     */
    SysPermission getById(Long id);

    /**
     * 指定された検索条件で権限をページング取得する。
     *
     * @param menuId 対象のメニューID
     * @param keyword 検索キーワード。未指定の場合は絞り込まない
     * @param request 処理対象のページング条件
     * @return ページングされた権限一覧
     */
    PageResult<SysPermission> page(Long menuId, String keyword, PageRequest request);

    /**
     * メニューに一致する権限を一覧取得する。
     *
     * @param menuId 対象のメニューID
     * @return 権限一覧。該当しない場合は空リスト
     */
    List<SysPermission> listByMenu(Long menuId);

    /**
     * 社員IDに一致する権限を一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 権限一覧。該当しない場合は空リスト
     */
    List<SysPermission> listByEmployeeId(Long employeeId);

    /**
     * 権限を新規作成する。
     *
     * @param command 処理対象の権限作成コマンド
     * @return 保存した権限
     */
    SysPermission create(PermissionCreateCommand command);

    /**
     * 権限を更新する。
     *
     * @param id 対象の権限ID
     * @param command 処理対象の権限更新コマンド
     * @return 更新した権限
     */
    SysPermission update(Long id, PermissionUpdateCommand command);

    /**
     * 権限を有効化する。
     *
     * @param id 対象の権限ID
     */
    void enable(Long id);

    /**
     * 権限を無効化する。
     *
     * @param id 対象の権限ID
     */
    void disable(Long id);

    /**
     * 指定IDの権限を削除する。
     *
     * @param id 対象の権限ID
     */
    void remove(Long id);
}

