package com.manpowergroup.kintai.employee.infrastructure.repository.org;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgNodeClosureRepository;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.employee.infrastructure.mapper.org.OrgNodeClosureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

// 組織閉包テーブルリポジトリ実装（infrastructure層）
@Repository
@RequiredArgsConstructor
public class OrgNodeClosureRepositoryImpl implements OrgNodeClosureRepository {

    private final OrgNodeClosureMapper mapper;

    @Override
    public List<OrgNodeClosure> findDescendants(Long ancestorId) {
        return mapper.selectList(Wrappers.<OrgNodeClosure>lambdaQuery()
                .eq(OrgNodeClosure::getAncestorId, ancestorId)
                .orderByAsc(OrgNodeClosure::getDepth));
    }

    @Override
    public List<OrgNodeClosure> findAncestors(Long descendantId) {
        return mapper.selectList(Wrappers.<OrgNodeClosure>lambdaQuery()
                .eq(OrgNodeClosure::getDescendantId, descendantId)
                .orderByAsc(OrgNodeClosure::getDepth));
    }

    @Override
    public void saveBatch(List<OrgNodeClosure> closures) {
        closures.forEach(mapper::insert);
    }

    @Override
    public void deleteByDescendantId(Long descendantId) {
        mapper.delete(Wrappers.<OrgNodeClosure>lambdaQuery()
                .eq(OrgNodeClosure::getDescendantId, descendantId));
    }

    @Override
    public void deleteExternalAncestorLinks(List<Long> subtreeNodeIds) {
        if (subtreeNodeIds == null || subtreeNodeIds.isEmpty()) {
            return;
        }
        mapper.delete(Wrappers.<OrgNodeClosure>lambdaQuery()
                .in(OrgNodeClosure::getDescendantId, subtreeNodeIds)
                .notIn(OrgNodeClosure::getAncestorId, subtreeNodeIds));
    }
}


