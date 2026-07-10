package com.manpowergroup.kintai.system.application.dto.sys.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "RoleAuthorizationSaveリクエスト")
public class RoleAuthorizationSaveRequest {

    @Schema(description = "メニューIDリスト")
    private List<Long> menuIds = new ArrayList<>();

    @Schema(description = "権限IDリスト")
    private List<Long> permissionIds = new ArrayList<>();
}
