package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.sys.MenuCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.MenuUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;

import java.util.List;

// メニューマスタサービス（アプリケーション層）
public interface SysMenuService  {

    /**
     * IDに対応するメニューを取得する。
     *
     * @param id 対象のメニューID
     * @return 取得したメニュー
     */
    SysMenu getById(Long id);

    /**
     * すべてのメニューを一覧取得する。
     *
     * @return メニュー一覧。該当しない場合は空リスト
     */
    List<SysMenu> listAll();

    /**
     * 社員IDに一致するメニューを一覧取得する。
     *
     * @param employeeId 対象の社員ID
     * @return メニュー一覧。該当しない場合は空リスト
     */
    List<SysMenu> listByEmployeeId(Long employeeId);

    /**
     * メニューを新規作成する。
     *
     * @param command 処理対象のメニュー作成コマンド
     * @return 保存したメニュー
     */
    SysMenu create(MenuCreateCommand command);

    /**
     * メニューを更新する。
     *
     * @param id 対象のメニューID
     * @param command 処理対象のメニュー更新コマンド
     * @return 更新したメニュー
     */
    SysMenu update(Long id, MenuUpdateCommand command);

    /**
     * メニューを表示状態に変更する。
     *
     * @param id 対象のメニューID
     */
    void show(Long id);

    /**
     * メニューを非表示状態に変更する。
     *
     * @param id 対象のメニューID
     */
    void hide(Long id);

    /**
     * メニューを有効化する。
     *
     * @param id 対象のメニューID
     */
    void enable(Long id);

    /**
     * メニューを無効化する。
     *
     * @param id 対象のメニューID
     */
    void disable(Long id);

    /**
     * 指定IDのメニューを削除する。
     *
     * @param id 対象のメニューID
     */
    void remove(Long id);
}

