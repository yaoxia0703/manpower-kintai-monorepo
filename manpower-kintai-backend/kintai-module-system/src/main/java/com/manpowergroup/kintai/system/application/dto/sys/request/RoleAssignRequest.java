package com.manpowergroup.kintai.system.application.dto.sys.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "RoleAssignリクエスト")
public class RoleAssignRequest {

    @NotEmpty(message = "付与対象は1件以上選択してください")
    @Schema(description = "IDリスト")
    private List<Long> ids;
}
