package com.manpowergroup.kintai.system.application.dto.sys.response;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnumValueResponse {

    private Long id;
    private String enumTypeCode;
    private String code;
    private Integer sort;
    private Status status;

    public static EnumValueResponse from(SysEnumValue enumValue) {
        return EnumValueResponse.builder()
                .id(enumValue.getId())
                .enumTypeCode(enumValue.getEnumTypeCode())
                .code(enumValue.getCode())
                .sort(enumValue.getSort())
                .status(enumValue.getStatus())
                .build();
    }
}
