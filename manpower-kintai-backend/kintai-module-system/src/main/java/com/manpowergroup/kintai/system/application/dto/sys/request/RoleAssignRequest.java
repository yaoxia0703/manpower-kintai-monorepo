package com.manpowergroup.kintai.system.application.dto.sys.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RoleAssignRequest {

    @NotEmpty(message = "付与対象は1件以上選択してください")
    private List<Long> ids;
}
