package com.manpowergroup.kintai.employee.application.dto.emp.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "EmployeePosition作成リクエスト")
public class EmployeePositionCreateRequest {

    @NotNull(message = "社員IDは必須です")
    @Schema(description = "社員ID")
    private Long employeeId;

    @NotNull(message = "会社は必須です")
    @Schema(description = "会社ID")
    private Long companyId;

    @NotNull(message = "所属組織は必須です")
    @Schema(description = "組織ノードID")
    private Long nodeId;

    @NotNull(message = "職級は必須です")
    @Schema(description = "職級ID")
    private Long gradeId;

    @NotNull(message = "主務設定は必須です")
    @Schema(description = "主務フラグ")
    private Integer isPrimary;

    @NotNull(message = "着任日は必須です")
    @Schema(description = "開始日")
    private LocalDate startDate;

    @Schema(description = "終了日")
    private LocalDate endDate;

    @Schema(description = "ステータス")
    private Status status;
}
