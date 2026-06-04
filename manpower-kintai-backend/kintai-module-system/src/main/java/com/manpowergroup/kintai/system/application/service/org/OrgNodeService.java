package com.manpowergroup.kintai.system.application.service.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.system.application.command.org.NodeUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;

import java.util.List;

public interface OrgNodeService {

    OrgNode getById(Long id);

    PageResult<OrgNode> pageByCompany(Long companyId, PageRequest request);

    List<OrgNode> listEnabledByCompany(Long companyId);

    OrgNode create(NodeCreateCommand command);

    OrgNode update(Long id, NodeUpdateCommand command);

    void enable(Long id);

    void disable(Long id);

    void remove(Long id);
}
