package com.manpowergroup.kintai.system.application.dto.sys;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuResponse {

    private Long id;
    private Long parentId;
    private String name;
    private String code;
    private String path;
    private String component;
    private String icon;
    private Integer type;
    private Integer sort;
    private Integer visible;
    private Status status;

    public static MenuResponse from(SysMenu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .parentId(menu.getParentId())
                .name(menu.getName())
                .code(menu.getCode())
                .path(menu.getPath())
                .component(menu.getComponent())
                .icon(menu.getIcon())
                .type(menu.getType())
                .sort(menu.getSort())
                .visible(menu.getVisible())
                .status(menu.getStatus())
                .build();
    }
}
