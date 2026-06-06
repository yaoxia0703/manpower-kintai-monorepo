package com.manpowergroup.kintai.system.infrastructure.repository.org;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrgNodeRepositoryImpl implements OrgNodeRepository {

    private final OrgNodeMapper mapper;

    @Override
    public Optional<OrgNode> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public boolean existsByCompanyAndCode(Long companyId, String code) {
        return mapper.selectCount(Wrappers.<OrgNode>lambdaQuery()
                .eq(OrgNode::getCompanyId, companyId)
                .eq(OrgNode::getCode, code)) > 0;
    }

    @Override
    public boolean existsByCompanyAndCodeExcludingId(Long companyId, String code, Long excludeId) {
        LambdaQueryWrapper<OrgNode> wrapper = Wrappers.<OrgNode>lambdaQuery()
                .eq(OrgNode::getCompanyId, companyId)
                .eq(OrgNode::getCode, code);
        if (excludeId != null) {
            wrapper.ne(OrgNode::getId, excludeId);
        }
        return mapper.selectCount(wrapper) > 0;
    }

    @Override
    public PageResult<OrgNode> findPageByCompany(Long companyId, int page, int size) {
        Page<OrgNode> p = new Page<>(page, size);
        mapper.selectPage(p, Wrappers.<OrgNode>lambdaQuery()
                .eq(OrgNode::getCompanyId, companyId)
                .orderByAsc(OrgNode::getSort));
        return PageResult.of(p);
    }

    @Override
    public List<OrgNode> listEnabledByCompany(Long companyId) {
        return mapper.selectList(Wrappers.<OrgNode>lambdaQuery()
                .eq(OrgNode::getCompanyId, companyId)
                .eq(OrgNode::getStatus, Status.ENABLED)
                .orderByAsc(OrgNode::getSort));
    }

    @Override
    public OrgNode save(OrgNode node) {
        mapper.insert(node);
        return node;
    }

    @Override
    public OrgNode update(OrgNode node) {
        mapper.updateById(node);
        return node;
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }
}
