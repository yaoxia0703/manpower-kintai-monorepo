package com.manpowergroup.kintai.system.application.dto.manager.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "SubordinateFilterOptionsレスポンス")
public class SubordinateFilterOptionsResponse {

    @Schema(description = "組織ノード候補リスト")
    private List<NodeOption> nodes;
    @Schema(description = "職級候補リスト")
    private List<GradeOption> grades;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GradeOption {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "名称")
        private String name;
        @Schema(description = "コード")
        private String code;
        @Schema(description = "職級レベル")
        private String gradeLevel;
    }
}
