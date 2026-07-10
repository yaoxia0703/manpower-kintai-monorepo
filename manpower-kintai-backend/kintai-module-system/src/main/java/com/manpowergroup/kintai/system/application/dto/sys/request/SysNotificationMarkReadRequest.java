package com.manpowergroup.kintai.system.application.dto.sys.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "通知既読リクエスト")
public record SysNotificationMarkReadRequest(
        @NotEmpty(message = "通知IDリストは必須です")
        @Schema(description = "既読通知の複数Id")
        List<Long> ids

) {
}
