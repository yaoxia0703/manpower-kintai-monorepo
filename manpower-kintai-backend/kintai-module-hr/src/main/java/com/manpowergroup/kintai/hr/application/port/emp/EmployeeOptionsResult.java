package com.manpowergroup.kintai.hr.application.port.emp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "EmployeeOptionResult")
public record EmployeeOptionsResult(
    @Schema(description = "選択中会社ID")
    Long selectedCompanyId,
    @Schema(description = "会社候補リスト")
    List<CompanyOption> companies,
    @Schema(description = "組織ノード候補リスト")
    List<NodeOption> nodes,
    @Schema(description = "職級候補リスト")
    List<GradeOption> grades,
    @Schema(description = "ロールリスト")
    List<RoleOption> roles
) {
    public record CompanyOption(
        Long id,
        Long parentId,
        String name,
        String companyCode
    ) {
    }

    ;

    public record NodeOption(
        Long id,
        Long parentId,
        String name,
        String code,
        String typeCode,
        Integer level
    ) {
    }

    public record GradeOption(
        Long id,
        String name,
        String code,
        String gradeLevel
    ) {
    }

    public record RoleOption(
        Long id,
        String code,
        String name
    ) {
    }

}




