package com.manpowergroup.kintai.employee.application.service.impl.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.employee.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.NodeUpdateCommand;
import com.manpowergroup.kintai.employee.application.service.org.OrgNodeService;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgNodeClosureRepository;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgNodeRepository;
import com.manpowergroup.kintai.employee.domain.service.org.OrgTreeDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

// 組織ノードサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class OrgNodeServiceImpl implements OrgNodeService {

    private final OrgNodeRepository nodeRepository;
    private final OrgNodeClosureRepository closureRepository;
    private final OrgTreeDomainService orgTreeDomainService;

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
        OrgNode parent = command.parentId() == null ? null : getById(command.parentId());
        OrgNode node = OrgNode.create(
                command.companyId(),
                parent,
                command.managerId(),
                command.name(),
                command.typeCode(),
                command.deptFunction(),
                command.code(),
                command.sort(),
                command.status());
        nodeRepository.save(node);

        List<OrgNodeClosure> parentAncestors = List.of();
        if (node.getParentId() != null) {
            parentAncestors = closureRepository.findAncestors(node.getParentId());
        }
        List<OrgNodeClosure> closures = orgTreeDomainService.buildClosuresForNewNode(node.getId(), parentAncestors);
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
        if (!Objects.equals(existing.getParentId(), command.parentId())) {
            List<OrgNodeClosure> subtree = closureRepository.findDescendants(id);
            if (orgTreeDomainService.containsNode(subtree, command.parentId())) {
                throw new BizException(SystemErrorCode.NODE_CYCLE);
            }
            moveSubtree(existing, command.parentId(), subtree);
        }
        existing.updateEditableFields(
                command.managerId(),
                command.name(),
                command.typeCode(),
                command.deptFunction(),
                command.code(),
                command.sort());
        return nodeRepository.update(existing);
    }

    private void moveSubtree(OrgNode node, Long newParentId, List<OrgNodeClosure> subtree) {
        OrgNode newParent = newParentId == null ? null : getById(newParentId);
        int previousLevel = node.getLevel();
        node.moveTo(newParent);
        int levelDelta = node.getLevel() - previousLevel;

        List<Long> subtreeNodeIds = subtree.stream()
                .map(OrgNodeClosure::getDescendantId)
                .toList();
        subtree.stream()
                .filter(closure -> closure.getDepth() != null && closure.getDepth() > 0)
                .map(OrgNodeClosure::getDescendantId)
                .map(this::getById)
                .forEach(descendant -> {
                    descendant.shiftLevel(levelDelta);
                    nodeRepository.update(descendant);
                });

        closureRepository.deleteExternalAncestorLinks(subtreeNodeIds);
        if (newParent != null) {
            List<OrgNodeClosure> parentAncestors = closureRepository.findAncestors(newParentId);
            List<OrgNodeClosure> newExternalClosures =
                    orgTreeDomainService.buildExternalClosuresForMovedSubtree(parentAncestors, subtree);
            closureRepository.saveBatch(newExternalClosures);
        }
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
        if (orgTreeDomainService.hasDescendants(closureRepository.findDescendants(id))) {
            throw new BizException(SystemErrorCode.NODE_HAS_CHILDREN);
        }
        closureRepository.deleteByDescendantId(id);
        nodeRepository.deleteById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        NODE_NOT_FOUND(404, "error.node.not_found"),
        NODE_CODE_DUPLICATE(409, "error.node.code_duplicate"),
        NODE_CYCLE(409, "error.node.cycle"),
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
