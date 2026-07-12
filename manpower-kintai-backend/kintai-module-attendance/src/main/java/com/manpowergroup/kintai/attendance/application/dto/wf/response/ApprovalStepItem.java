package com.manpowergroup.kintai.attendance.application.dto.wf.response;

import com.manpowergroup.kintai.attendance.domain.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalStepItem {

    private Integer step;
    private Long approverId;
    private ApprovalStatus status;
    private String comment;
    private LocalDateTime decidedAt;
}
