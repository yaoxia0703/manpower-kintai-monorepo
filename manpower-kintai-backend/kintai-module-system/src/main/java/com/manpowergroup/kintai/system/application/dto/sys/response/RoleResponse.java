package com.manpowergroup.kintai.system.application.dto.sys.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Roleレスポンス")
public class RoleResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "会社ID")
    private Long companyId;
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

    public static RoleResponse from(SysRole role) {
        return RoleResponse.builder()
                .id(role.getId())
                .companyId(role.getCompanyId())
                .code(role.getCode())
                .name(role.getName())
                .remark(role.getRemark())
                .sort(role.getSort())
                .status(role.getStatus())
                .build();
    }
}
