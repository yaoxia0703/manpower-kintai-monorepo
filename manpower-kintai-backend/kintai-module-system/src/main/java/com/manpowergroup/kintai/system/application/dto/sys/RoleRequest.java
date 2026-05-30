package com.manpowergroup.kintai.system.application.dto.sys;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleRequest {

    private Long companyId;

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String remark;

    @NotNull
    private Integer sort;

    public SysRole toEntity() {
        return new SysRole()
                .setCompanyId(companyId)
                .setCode(code)
                .setName(name)
                .setRemark(remark)
                .setSort(sort)
                .setStatus(Status.ENABLED);
    }
}
