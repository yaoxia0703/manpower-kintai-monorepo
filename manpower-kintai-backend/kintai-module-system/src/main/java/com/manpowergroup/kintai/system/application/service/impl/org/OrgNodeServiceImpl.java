package com.manpowergroup.kintai.system.application.service.impl.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.system.application.command.org.NodeUpdateCommand;
import com.manpowergroup.kintai.system.application.service.org.OrgNodeService;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeClosureRepository;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 組織ノードサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class OrgNodeServiceImpl implements OrgNodeService {

    private final OrgNodeRepository nodeRepository;
    private final OrgNodeClosureRepository closureRepository;

    @Override
    public OrgNode getById(Long id) {
        return nodeRepository.findById(id)
                .orElseThrow(() -> new BizException(SystemErrorCode.NODE_NOT_FOUND));
    }

    @Override
    public PageResult<OrgNode> pageByCompany(Long companyId, PageRequest request) {
        return nodeRepository.findPageByCompany(companyId, request.page(), request.size());
    }

    @Override
    public List<OrgNode> listEnabledByCompany(Long companyId) {
        return nodeRepository.listEnabledByCompany(companyId);
    }

    @Override
    @Transactional
    public OrgNode create(NodeCreateCommand command) {
        if (nodeRepository.existsByCompanyAndCode(command.companyId(), command.code())) {
            throw new BizException(SystemErrorCode.NODE_CODE_DUPLICATE);
        }
        OrgNode node = new OrgNode()
                .setCompanyId(command.companyId())
                .setParentId(command.parentId())
                .setManagerId(command.managerId())
                .setName(command.name())
                .setTypeCode(command.typeCode())
                .setDeptFunction(command.deptFunction())
                .setCode(command.code())
                .setLevel(command.level())
                .setSort(command.sort())
                .setStatus(command.status() == null ? Status.ENABLED : command.status());
        nodeRepository.save(node);

        List<OrgNodeClosure> closures = new ArrayList<>();
        closures.add(new OrgNodeClosure(node.getId(), node.getId(), 0));

        if (node.getParentId() != null) {
            List<OrgNodeClosure> parentAncestors = closureRepository.findAncestors(node.getParentId());
            for (OrgNodeClosure ancestor : parentAncestors) {
                closures.add(new OrgNodeClosure(ancestor.getAncestorId(), node.getId(), ancestor.getDepth() + 1));
            }
        }
        closureRepository.saveBatch(closures);
        return node;
    }

    @Override
    @Transactional
    public OrgNode update(Long id, NodeUpdateCommand command) {
        OrgNode existing = getById(id);
        if (nodeRepository.existsByCompanyAndCodeExcludingId(command.companyId(), command.code(), id)) {
            throw new BizException(SystemErrorCode.NODE_CODE_DUPLICATE);
        }
        existing.setName(command.name())
                .setTypeCode(command.typeCode())
                .setDeptFunction(command.deptFunction())
                .setCode(command.code())
                .setManagerId(command.managerId())
                .setSort(command.sort());
        return nodeRepository.update(existing);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        OrgNode node = getById(id);
        node.enable();
        nodeRepository.update(node);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        OrgNode node = getById(id);
        node.disable();
        nodeRepository.update(node);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        boolean hasDescendants = closureRepository.findDescendants(id)
                .stream()
                .anyMatch(closure -> closure.getDepth() != null && closure.getDepth() > 0);
        if (hasDescendants) {
            throw new BizException(SystemErrorCode.NODE_HAS_CHILDREN);
        }
        closureRepository.deleteByDescendantId(id);
        nodeRepository.deleteById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        NODE_NOT_FOUND(404, "error.node.not_found"),
        NODE_CODE_DUPLICATE(409, "error.node.code_duplicate"),
        NODE_HAS_CHILDREN(409, "error.node.has_children");

        private final int code;
        private final String messageKey;

        SystemErrorCode(int code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}
