package com.manpowergroup.kintai.system.application.dto.manager.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "SubordinateEmployeeレスポンス")
public class SubordinateEmployeeResponse {

    @Schema(description = "社員ID")
    private Long employeeId;
    @Schema(description = "社員番号")
    private String employeeCode;
    @Schema(description = "表示名")
    private String displayName;
    @Schema(description = "メールアドレス")
    private String email;
    @Schema(description = "会社ID")
    private Long companyId;
    @Schema(description = "組織ノードID")
    private Long nodeId;
    @Schema(description = "node Name")
    private String nodeName;
    @Schema(description = "職級ID")
    private Long gradeId;
    @Schema(description = "grade Name")
    private String gradeName;
}
