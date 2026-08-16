package com.manpowergroup.kintai.employee.application.dto.directory;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "全社員一覧リスボンス(HR)")
public record EmployeeDirectoryResponse(

    Long employeeId,
    @Schema(description = "社員番号")
    String employeeCode,
    @Schema(description = "社員氏名")
    String displayName,
    @Schema(description = "社員氏名(カタカナ)")
    String displayNameKana,
    @Schema(description = "メールアドレス")
    String email,
    @Schema(description = "会社ID")
    Long companyId,
    @Schema(description = "会社ID")
    String companyName,
    @Schema(description = "組織ノードID")
    Long nodeId,
    @Schema(description = "node Name")
    String nodeName,
    @Schema(description = "職級ID")
    Long gradeId,
    @Schema(description = "職級 Name")
    String gradeName,
    @Schema(description = "電話")
    String phone,
    @Schema(description = "性別")
    Long gender,
    Integer sort


) {
}
