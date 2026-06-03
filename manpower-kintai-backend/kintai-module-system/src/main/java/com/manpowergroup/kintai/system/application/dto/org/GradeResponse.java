package com.manpowergroup.kintai.system.application.dto.org;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradeResponse {

    private Long id;
    private Long companyId;
    private String name;
    private String code;
    private String gradeLevel;
    private Integer sort;
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
