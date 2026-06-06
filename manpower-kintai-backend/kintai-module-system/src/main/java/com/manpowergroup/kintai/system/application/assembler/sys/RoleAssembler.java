package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.RoleCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.RoleUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.RoleResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.RoleUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

public final class RoleAssembler {

    private RoleAssembler() {
    }

    public static RoleCreateCommand toCommand(RoleCreateRequest request) {
        return new RoleCreateCommand(
                request.getCompanyId(),
                request.getCode(),
                request.getName(),
                request.getRemark(),
                request.getSort()
        );
    }

    public static RoleUpdateCommand toCommand(RoleUpdateRequest request) {
        return new RoleUpdateCommand(
                request.getCompanyId(),
                request.getCode(),
                request.getName(),
                request.getRemark(),
                request.getSort()
        );
    }

    public static RoleResponse toResponse(SysRole role) {
        return RoleResponse.from(role);
    }
}
