package com.manpowergroup.kintai.system.application.dto.sys;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MenuRequest {

    private Long parentId;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String code;

    @Size(max = 255)
    private String path;

    @Size(max = 255)
    private String component;

    @Size(max = 100)
    private String icon;

    @NotNull
    @Min(1)
    @Max(3)
    private Integer type;

    @NotNull
    private Integer sort;

    @Min(0)
    @Max(1)
    private Integer visible;

    public SysMenu toEntity() {
        return new SysMenu()
                .setParentId(parentId)
                .setName(name)
                .setCode(code)
                .setPath(path)
                .setComponent(component)
                .setIcon(icon)
                .setType(type)
                .setSort(sort)
                .setVisible(visible == null ? 1 : visible)
                .setStatus(Status.ENABLED);
    }
}
