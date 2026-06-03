package com.manpowergroup.kintai.system.application.dto.sys;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {

    private Long id;
    private Long menuId;
    private String code;
    private String name;
    private String method;
    private String path;
    private String remark;
    private Integer sort;
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
