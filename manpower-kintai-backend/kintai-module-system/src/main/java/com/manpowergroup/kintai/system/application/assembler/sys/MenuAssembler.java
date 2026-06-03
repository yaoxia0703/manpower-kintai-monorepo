package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.MenuCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.MenuUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.MenuResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.MenuCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.MenuUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;

public final class MenuAssembler {

    private MenuAssembler() {
    }

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

    public static MenuResponse toResponse(SysMenu menu) {
        return MenuResponse.from(menu);
    }
}
