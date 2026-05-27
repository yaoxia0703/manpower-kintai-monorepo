package com.manpowergroup.kintai.system.application.service.impl.org;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeClosureRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeMapper;
import com.manpowergroup.kintai.system.application.service.org.OrgNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 組織ノードサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class OrgNodeServiceImpl extends ServiceImpl<OrgNodeMapper, OrgNode>
        implements OrgNodeService {

    private final OrgNodeClosureRepository closureRepository;

    @Override
    public OrgNode getById(Long id) {
        OrgNode node = super.getById(id);
        if (node == null) throw new BizException(SystemErrorCode.NODE_NOT_FOUND);
        return node;
    }

    @Override
    public PageResult<OrgNode> pageByCompany(Long companyId, PageRequest request) {
        Page<OrgNode> p = new Page<>(request.page(), request.size());
        page(p, lambdaQuery()
                .eq(OrgNode::getCompanyId, companyId)
                .orderByAsc(OrgNode::getSort)
                .getWrapper());
        return PageResult.of(p);
    }

    @Override
    public List<OrgNode> listEnabledByCompany(Long companyId) {
        return lambdaQuery()
                .eq(OrgNode::getCompanyId, companyId)
                .eq(OrgNode::getStatus, Status.ENABLED)
                .orderByAsc(OrgNode::getSort)
                .list();
    }

    @Override
    @Transactional
    public OrgNode create(OrgNode node) {
        boolean exists = lambdaQuery()
                .eq(OrgNode::getCompanyId, node.getCompanyId())
                .eq(OrgNode::getCode, node.getCode())
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.NODE_CODE_DUPLICATE);
        save(node);

        // Closure Table更新：自分自身のレコード（depth=0）を追加
        List<OrgNodeClosure> closures = new ArrayList<>();
        closures.add(new OrgNodeClosure(node.getId(), node.getId(), 0));

        // 親ノードが存在する場合、祖先レコードを継承
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
    public OrgNode update(Long id, OrgNode node) {
        OrgNode existing = getById(id);
        boolean exists = lambdaQuery()
                .eq(OrgNode::getCompanyId, node.getCompanyId())
                .eq(OrgNode::getCode, node.getCode())
                .ne(OrgNode::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.NODE_CODE_DUPLICATE);
        existing.setName(node.getName())
                .setTypeCode(node.getTypeCode())
                .setDeptFunction(node.getDeptFunction())
                .setCode(node.getCode())
                .setManagerId(node.getManagerId())
                .setSort(node.getSort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        OrgNode node = getById(id);
        node.setStatus(Status.ENABLED);
        updateById(node);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        OrgNode node = getById(id);
        node.setStatus(Status.DISABLED);
        updateById(node);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        // Closure Tableから削除してからノードを論理削除
        closureRepository.deleteByDescendantId(id);
        removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        NODE_NOT_FOUND(404, "error.node.not_found"),
        NODE_CODE_DUPLICATE(409, "error.node.code_duplicate");

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

