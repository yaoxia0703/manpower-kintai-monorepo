package com.manpowergroup.kintai.system.application.dto.hr.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.system.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "EmployeeOnboardingOptionsレスポンス")
public class EmployeeOnboardingOptionsResponse {

    @Schema(description = "選択中会社ID")
    private Long selectedCompanyId;
    @Schema(description = "会社候補リスト")
    private List<CompanyOption> companies;
    @Schema(description = "組織ノード候補リスト")
    private List<NodeOption> nodes;
    @Schema(description = "職級候補リスト")
    private List<GradeOption> grades;
    @Schema(description = "ロールリスト")
    private List<RoleOption> roles;

    @Data
    @Builder
    public static class CompanyOption {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "親ID")
        private Long parentId;
        @Schema(description = "名称")
        private String name;
        @Schema(description = "会社コード")
        private String companyCode;

        public static CompanyOption from(OrgCompany company) {
            return CompanyOption.builder()
                    .id(company.getId())
                    .parentId(company.getParentId())
                    .name(company.getName())
                    .companyCode(company.getCompanyCode())
                    .build();
        }
    }

    @Data
    @Builder
    public static class NodeOption {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "親ID")
        private Long parentId;
        @Schema(description = "名称")
        private String name;
        @Schema(description = "コード")
        private String code;
        @Schema(description = "タイプコード")
        private String typeCode;
        @Schema(description = "階層レベル")
        private Integer level;

        public static NodeOption from(OrgNode node) {
            return NodeOption.builder()
                    .id(node.getId())
                    .parentId(node.getParentId())
                    .name(node.getName())
                    .code(node.getCode())
                    .typeCode(node.getTypeCode())
                    .level(node.getLevel())
                    .build();
        }
    }

    @Data
    @Builder
    public static class GradeOption {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "名称")
        private String name;
        @Schema(description = "コード")
        private String code;
        @Schema(description = "職級レベル")
        private String gradeLevel;

        public static GradeOption from(OrgGrade grade) {
            return GradeOption.builder()
                    .id(grade.getId())
                    .name(grade.getName())
                    .code(grade.getCode())
                    .gradeLevel(grade.getGradeLevel())
                    .build();
        }
    }

    @Data
    @Builder
    public static class RoleOption {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "コード")
        private String code;
        @Schema(description = "名称")
        private String name;

        public static RoleOption from(SysRole role) {
            return RoleOption.builder()
                    .id(role.getId())
                    .code(role.getCode())
                    .name(role.getName())
                    .build();
        }
    }
}
