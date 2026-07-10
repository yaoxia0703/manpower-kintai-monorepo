package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Menuレスポンス")
public class MenuResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "親ID")
    private Long parentId;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "コード")
    private String code;
    @Schema(description = "パス")
    private String path;
    @Schema(description = "コンポーネント")
    private String component;
    @Schema(description = "アイコン")
    private String icon;
    @Schema(description = "タイプ")
    private Integer type;
    @Schema(description = "表示順")
    private Integer sort;
    @Schema(description = "表示フラグ")
    private Integer visible;
    @Schema(description = "ステータス")
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
