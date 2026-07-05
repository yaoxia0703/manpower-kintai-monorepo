package com.manpowergroup.kintai.system.application.dto.manager.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubordinateFilterOptionsResponse {

    private List<NodeOption> nodes;
    private List<GradeOption> grades;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NodeOption {
        private Long id;
        private Long parentId;
        private String name;
        private String code;
        private String typeCode;
        private Integer level;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradeOption {
        private Long id;
        private String name;
        private String code;
        private String gradeLevel;
    }
}
