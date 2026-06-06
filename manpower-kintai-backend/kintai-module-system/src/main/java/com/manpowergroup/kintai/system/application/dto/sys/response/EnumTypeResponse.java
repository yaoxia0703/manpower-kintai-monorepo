package com.manpowergroup.kintai.system.application.dto.sys.response;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnumTypeResponse {

    private Long id;
    private String code;
    private String name;
    private String remark;
    private Integer sort;
    private Status status;

    public static EnumTypeResponse from(SysEnumType enumType) {
        return EnumTypeResponse.builder()
                .id(enumType.getId())
                .code(enumType.getCode())
                .name(enumType.getName())
                .remark(enumType.getRemark())
                .sort(enumType.getSort())
                .status(enumType.getStatus())
                .build();
    }
}
