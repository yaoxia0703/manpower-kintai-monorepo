package com.manpowergroup.kintai.attendance.application.dto.wf.request;

import jakarta.validation.constraints.Size;

public record ApprovalDecisionRequest(
        @Size(max = 500, message = "承認コメントは500文字以内にしてください")
        String comment
) {
}
