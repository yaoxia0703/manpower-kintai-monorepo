package com.manpowergroup.kintai.system.infrastructure.mapper.org;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 組織閉包テーブルMapper
@Mapper
public interface OrgNodeClosureMapper extends BaseMapper<OrgNodeClosure> {

    // 指定ノードの全子孫を取得（depth昇順）
    List<OrgNodeClosure> selectDescendants(@Param("ancestorId") Long ancestorId);

    // 指定ノードの全祖先を取得（depth昇順）
    List<OrgNodeClosure> selectAncestors(@Param("descendantId") Long descendantId);

    // 指定ノードとその子孫に関連するClosureレコードを全削除
    int deleteByDescendantId(@Param("descendantId") Long descendantId);
}


