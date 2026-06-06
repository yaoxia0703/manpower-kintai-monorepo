package com.manpowergroup.kintai.system.application.dto.hr.response;

import com.manpowergroup.kintai.system.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.system.domain.entity.org.OrgGrade;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmployeeOnboardingOptionsResponse {

    private Long selectedCompanyId;
    private List<CompanyOption> companies;
    private List<NodeOption> nodes;
    private List<GradeOption> grades;
    private List<RoleOption> roles;

    @Data
    @Builder
    public static class CompanyOption {
        private Long id;
        private Long parentId;
        private String name;
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
        private Long id;
        private Long parentId;
        private String name;
        private String code;
        private String typeCode;
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
        private Long id;
        private String name;
        private String code;
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
        private Long id;
        private String code;
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
