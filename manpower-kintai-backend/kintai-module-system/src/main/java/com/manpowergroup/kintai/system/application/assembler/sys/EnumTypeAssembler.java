package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.EnumTypeCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumTypeUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.EnumTypeResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumTypeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumTypeUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;

public final class EnumTypeAssembler {

    private EnumTypeAssembler() {
    }

    public static EnumTypeCreateCommand toCommand(EnumTypeCreateRequest request) {
        return new EnumTypeCreateCommand(
                request.getCode(),
                request.getName(),
                request.getRemark(),
                request.getSort(),
                request.getStatus()
        );
    }

    public static EnumTypeUpdateCommand toCommand(EnumTypeUpdateRequest request) {
        return new EnumTypeUpdateCommand(
                request.getCode(),
                request.getName(),
                request.getRemark(),
                request.getSort(),
                request.getStatus()
        );
    }

    public static EnumTypeResponse toResponse(SysEnumType enumType) {
        return EnumTypeResponse.from(enumType);
    }
}
