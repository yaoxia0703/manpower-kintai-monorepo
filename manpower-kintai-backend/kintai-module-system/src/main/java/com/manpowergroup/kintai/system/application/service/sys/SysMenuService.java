package com.manpowergroup.kintai.system.application.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.sys.MenuCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.MenuUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;

import java.util.List;

// メニューマスタサービス（アプリケーション層）
public interface SysMenuService extends IService<SysMenu> {

    // IDでメニューを取得（存在しない場合は例外）
    SysMenu getById(Long id);

    // 全メニューを取得（ツリー構築用）
    List<SysMenu> listAll();

    // 社員IDに基づくメニュー一覧を取得（ロール経由）
    List<SysMenu> listByEmployeeId(Long employeeId);

    // メニューを新規作成
    SysMenu create(MenuCreateCommand command);

    // メニューを更新
    SysMenu update(Long id, MenuUpdateCommand command);

    // メニューを表示
    void show(Long id);

    // メニューを非表示
    void hide(Long id);

    // メニューを有効化
    void enable(Long id);

    // メニューを無効化
    void disable(Long id);

    // メニューを削除（論理削除）
    void remove(Long id);
}

