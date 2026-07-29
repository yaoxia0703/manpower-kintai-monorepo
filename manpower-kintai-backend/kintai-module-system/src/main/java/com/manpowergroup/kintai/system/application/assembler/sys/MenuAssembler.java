package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.MenuCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.MenuUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.MenuResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.MenuCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.MenuUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;

public final class MenuAssembler {

    private MenuAssembler() {
    }

    /**
     * 入力データをメニュー作成コマンドへ変換する。
     *
     * @param request 変換対象のメニュー作成リクエスト
     * @return 変換後のメニュー作成コマンド
     */
    public static MenuCreateCommand toCommand(MenuCreateRequest request) {
        return new MenuCreateCommand(
                request.getParentId(),
                request.getName(),
                request.getCode(),
                request.getPath(),
                request.getComponent(),
                request.getIcon(),
                request.getType(),
                request.getSort(),
                request.getVisible()
        );
    }

    /**
     * 入力データをメニュー更新コマンドへ変換する。
     *
     * @param request 変換対象のメニュー更新リクエスト
     * @return 変換後のメニュー更新コマンド
     */
    public static MenuUpdateCommand toCommand(MenuUpdateRequest request) {
        return new MenuUpdateCommand(
                request.getParentId(),
                request.getName(),
                request.getCode(),
                request.getPath(),
                request.getComponent(),
                request.getIcon(),
                request.getType(),
                request.getSort(),
                request.getVisible()
        );
    }

    /**
     * 入力データをメニューレスポンスへ変換する。
     *
     * @param menu 処理対象のメニュー
     * @return 変換後のメニューレスポンス
     */
    public static MenuResponse toResponse(SysMenu menu) {
        return MenuResponse.from(menu);
    }
}
