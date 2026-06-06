package com.manpowergroup.kintai.system.application.dto.org.response;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeResponse {

    private Long id;
    private Long companyId;
    private Long parentId;
    private Long managerId;
    private String name;
    private String typeCode;
    private String deptFunction;
    private String code;
    private Integer level;
    private Integer sort;
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
