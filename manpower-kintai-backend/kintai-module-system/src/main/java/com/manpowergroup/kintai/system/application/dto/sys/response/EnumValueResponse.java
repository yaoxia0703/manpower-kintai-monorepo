package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "EnumValueレスポンス")
public class EnumValueResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "列挙型コード")
    private String enumTypeCode;
    @Schema(description = "コード")
    private String code;
    @Schema(description = "表示順")
    private Integer sort;
    @Schema(description = "ステータス")
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
