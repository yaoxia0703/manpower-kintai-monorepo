package com.manpowergroup.kintai.system.infrastructure.repository.org;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeClosureRepository;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.system.infrastructure.mapper.org.OrgNodeClosureMapper;
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
        return mapper.selectDescendants(ancestorId);
    }

    @Override
    public List<OrgNodeClosure> findAncestors(Long descendantId) {
        return mapper.selectAncestors(descendantId);
    }

    @Override
    public void saveBatch(List<OrgNodeClosure> closures) {
        closures.forEach(mapper::insert);
    }

    @Override
    public void deleteByDescendantId(Long descendantId) {
        mapper.deleteByDescendantId(descendantId);
    }
}


