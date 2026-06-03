package com.manpowergroup.kintai.system.application.dto.emp.request;

import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeePositionCreateRequest {

    @NotNull(message = "社員IDは必須です")
    private Long employeeId;

    @NotNull(message = "会社は必須です")
    private Long companyId;

    @NotNull(message = "所属組織は必須です")
    private Long nodeId;

    @NotNull(message = "職級は必須です")
    private Long gradeId;

    @NotNull(message = "主務設定は必須です")
    private Integer isPrimary;

    @NotNull(message = "着任日は必須です")
    private LocalDate startDate;

    private LocalDate endDate;

    private Status status;
}
