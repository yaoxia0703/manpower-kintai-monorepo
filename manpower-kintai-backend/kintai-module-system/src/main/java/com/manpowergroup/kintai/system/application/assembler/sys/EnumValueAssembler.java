package com.manpowergroup.kintai.system.application.assembler.sys;

import com.manpowergroup.kintai.system.application.command.sys.EnumValueCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.sys.response.EnumValueResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumValueCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.EnumValueUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;

public final class EnumValueAssembler {

    private EnumValueAssembler() {
    }

    public static EnumValueCreateCommand toCommand(EnumValueCreateRequest request) {
        return new EnumValueCreateCommand(
                request.getEnumTypeCode(),
                request.getCode(),
                request.getSort(),
                request.getStatus()
        );
    }

    public static EnumValueUpdateCommand toCommand(EnumValueUpdateRequest request) {
        return new EnumValueUpdateCommand(
                request.getEnumTypeCode(),
                request.getCode(),
                request.getSort(),
                request.getStatus()
        );
    }

    public static EnumValueResponse toResponse(SysEnumValue enumValue) {
        return EnumValueResponse.from(enumValue);
    }
}
