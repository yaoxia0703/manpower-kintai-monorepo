package com.manpowergroup.kintai.employee.application.dto.org.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Nodeレスポンス")
public class NodeResponse {

    @Schema(description = "ID")
    private Long id;
    @Schema(description = "会社ID")
    private Long companyId;
    @Schema(description = "親ID")
    private Long parentId;
    @Schema(description = "ノード責任者社員ID")
    private Long managerId;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "タイプコード")
    private String typeCode;
    @Schema(description = "部門機能区分")
    private String deptFunction;
    @Schema(description = "コード")
    private String code;
    @Schema(description = "階層レベル")
    private Integer level;
    @Schema(description = "表示順")
    private Integer sort;
    @Schema(description = "ステータス")
    private Status status;

    public static NodeResponse from(OrgNode node) {
        return NodeResponse.builder()
                .id(node.getId())
                .companyId(node.getCompanyId())
                .parentId(node.getParentId())
                .managerId(node.getManagerId())
                .name(node.getName())
                .typeCode(node.getTypeCode())
                .deptFunction(node.getDeptFunction())
                .code(node.getCode())
                .level(node.getLevel())
                .sort(node.getSort())
                .status(node.getStatus())
                .build();
    }
}
