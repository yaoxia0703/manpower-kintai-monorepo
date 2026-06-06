package com.manpowergroup.kintai.system.application.dto.sys.response;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {

    private Long id;
    private Long companyId;
    private String code;
    private String name;
    private String remark;
    private Integer sort;
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
