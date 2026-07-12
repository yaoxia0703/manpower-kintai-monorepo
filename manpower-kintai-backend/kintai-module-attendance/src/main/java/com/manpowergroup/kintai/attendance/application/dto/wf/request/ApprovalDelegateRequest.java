package com.manpowergroup.kintai.attendance.application.dto.wf.request;

import jakarta.validation.constraints.NotNull;

public record ApprovalDelegateRequest(
        @NotNull(message = "委譲先承認者は必須です")
        Long targetApproverId
) {
}
