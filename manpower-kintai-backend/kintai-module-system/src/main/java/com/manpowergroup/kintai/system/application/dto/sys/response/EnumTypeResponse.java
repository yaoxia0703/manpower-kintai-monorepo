package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "EnumTypeレスポンス")
public class EnumTypeResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "コード")
    private String code;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "備考")
    private String remark;
    @Schema(description = "表示順")
    private Integer sort;
    @Schema(description = "ステータス")
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
