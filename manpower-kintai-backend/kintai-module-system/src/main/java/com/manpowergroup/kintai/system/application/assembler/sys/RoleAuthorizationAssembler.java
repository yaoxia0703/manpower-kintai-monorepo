package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleAuthorizationSaveCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleMenuAssignCommand;
import com.manpowergroup.kintai.system.application.command.sys.RolePermissionAssignCommand;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleAssignRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleAuthorizationSaveRequest;

public final class RoleAuthorizationAssembler {

    private RoleAuthorizationAssembler() {
    }

    public static RoleMenuAssignCommand toMenuAssignCommand(Long roleId, RoleAssignRequest request) {
        return new RoleMenuAssignCommand(roleId, request == null ? null : request.getIds());
    }

    public static RolePermissionAssignCommand toPermissionAssignCommand(Long roleId, RoleAssignRequest request) {
        return new RolePermissionAssignCommand(roleId, request == null ? null : request.getIds());
    }

    public static RoleAuthorizationSaveCommand toSaveCommand(Long roleId, RoleAuthorizationSaveRequest request) {
        return new RoleAuthorizationSaveCommand(
                roleId,
                request == null ? null : request.getMenuIds(),
                request == null ? null : request.getPermissionIds()
        );
    }
}
