package com.manpowergroup.kintai.system.domain.entity.org;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 組織閉包テーブル（Closure Table）
// 任意の祖先・子孫ペアを保持し、組織ツリーの高速クエリを実現
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("org_node_closure")
/** 組織ノード間の祖先・子孫関係と階層の深さを表す Closure Table の行。 */
public class OrgNodeClosure {

    // 祖先ノードID
    private Long ancestorId;

    // 子孫ノードID
    private Long descendantId;

    // 階層距離（0=自分自身）
    private Integer depth;
}

