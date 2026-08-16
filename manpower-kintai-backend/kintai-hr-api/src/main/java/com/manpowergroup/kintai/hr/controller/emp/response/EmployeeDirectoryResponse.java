package com.manpowergroup.kintai.hr.controller.emp.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "社員一覧レスポンス")
public record EmployeeDirectoryResponse(

    @Schema(description = "社員ID")
    Long employeeId,

    @Schema(description = "社員番号")
    String employeeCode,

    @Schema(description = "氏名")
    String displayName,

    @Schema(description = "氏名（カナ）")
    String displayNameKana,

    @Schema(description = "メールアドレス")
    String email,

    @Schema(description = "会社ID")
    Long companyId,

    @Schema(description = "会社名")
    String companyName,

    @Schema(description = "所属組織ID")
    Long nodeId,

    @Schema(description = "所属組織名")
    String nodeName,

    @Schema(description = "職級ID")
    Long gradeId,

    @Schema(description = "職級名")
    String gradeName,

    @Schema(description = "電話番号")
    String phone,

    @Schema(description = "性別")
    Long gender,
    Integer sort
) {
}
