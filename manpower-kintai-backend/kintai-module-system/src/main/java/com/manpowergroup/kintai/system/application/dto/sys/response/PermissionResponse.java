package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.PermissionHttpMethod;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Permissionレスポンス")
public class PermissionResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "メニューID")
    private Long menuId;
    @Schema(description = "コード")
    private String code;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "HTTPメソッド")
    private PermissionHttpMethod method;
    @Schema(description = "パス")
    private String path;
    @Schema(description = "備考")
    private String remark;
    @Schema(description = "表示順")
    private Integer sort;
    @Schema(description = "ステータス")
    private Status status;

    public static PermissionResponse from(SysPermission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .menuId(permission.getMenuId())
                .code(permission.getCode())
                .name(permission.getName())
                .method(permission.getMethod())
                .path(permission.getPath())
                .remark(permission.getRemark())
                .sort(permission.getSort())
                .status(permission.getStatus())
                .build();
    }
}
