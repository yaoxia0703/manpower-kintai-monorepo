package com.manpowergroup.kintai.system.application.dto.org.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Gradeレスポンス")
public class GradeResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "会社ID")
    private Long companyId;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "コード")
    private String code;
    @Schema(description = "職級レベル")
    private String gradeLevel;
    @Schema(description = "表示順")
    private Integer sort;
    @Schema(description = "ステータス")
    private Status status;

    public static GradeResponse from(OrgGrade grade) {
        return GradeResponse.builder()
                .id(grade.getId())
                .companyId(grade.getCompanyId())
                .name(grade.getName())
                .code(grade.getCode())
                .gradeLevel(grade.getGradeLevel())
                .sort(grade.getSort())
                .status(grade.getStatus())
                .build();
    }
}
