package com.manpowergroup.kintai.hr.controller.emp.request;

import com.manpowergroup.kintai.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "全社員一覧リクエスト")
public record EmployeeDirectoryRequest(
    @Schema(description = "会社ID")//現在点一社のみで、使用しない。以後複数子会社があれば、使用可能
    Long companyId,
    String keyword,
    Long nodeId,
    Long gradeId,
    Status status,

    @Min(1)
    Integer page,

    @Min(1)
    @Max(100)
    Integer size
) {
    public EmployeeDirectoryRequest {
        if (page == null) page = 1;
        if (size == null) size = 10;
    }
}
