package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.PermissionCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.PermissionUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.PermissionResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.PermissionCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.PermissionUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysPermission;

public final class PermissionAssembler {

    private PermissionAssembler() {
    }

    public static PermissionCreateCommand toCommand(PermissionCreateRequest request) {
        return new PermissionCreateCommand(
                request.getMenuId(),
                request.getCode(),
                request.getName(),
                request.getMethod(),
                request.getPath(),
                request.getRemark(),
                request.getSort()
        );
    }

    public static PermissionUpdateCommand toCommand(PermissionUpdateRequest request) {
        return new PermissionUpdateCommand(
                request.getMenuId(),
                request.getCode(),
                request.getName(),
                request.getMethod(),
                request.getPath(),
                request.getRemark(),
                request.getSort()
        );
    }

    public static PermissionResponse toResponse(SysPermission permission) {
        return PermissionResponse.from(permission);
    }
}
