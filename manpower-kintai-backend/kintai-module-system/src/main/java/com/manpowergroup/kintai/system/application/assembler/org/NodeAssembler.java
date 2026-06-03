package com.manpowergroup.kintai.system.application.assembler.org;

import com.manpowergroup.kintai.system.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.system.application.command.org.NodeUpdateCommand;
import com.manpowergroup.kintai.system.application.dto.org.NodeResponse;
import com.manpowergroup.kintai.system.application.dto.org.request.NodeCreateRequest;
import com.manpowergroup.kintai.system.application.dto.org.request.NodeUpdateRequest;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;

public final class NodeAssembler {

    private NodeAssembler() {
    }

    public static NodeCreateCommand toCommand(NodeCreateRequest request) {
        return new NodeCreateCommand(
                request.getCompanyId(), request.getParentId(), request.getManagerId(), request.getName(),
                request.getTypeCode(), request.getDeptFunction(), request.getCode(), request.getLevel(),
                request.getSort(), request.getStatus()
        );
    }

    public static NodeUpdateCommand toCommand(NodeUpdateRequest request) {
        return new NodeUpdateCommand(
                request.getCompanyId(), request.getParentId(), request.getManagerId(), request.getName(),
                request.getTypeCode(), request.getDeptFunction(), request.getCode(), request.getLevel(),
                request.getSort(), request.getStatus()
        );
    }

    public static NodeResponse toResponse(OrgNode node) {
        return NodeResponse.from(node);
    }
}
