package com.manpowergroup.kintai.employee.domain.service.org;

import com.manpowergroup.kintai.employee.domain.entity.org.OrgNodeClosure;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/** 組織 Closure Table の構築とサブツリー移動計算を担うドメインサービス。 */
public class OrgTreeDomainService {

    /**
     * 新規組織ノードの階層関係を構築する。
     *
     * @param nodeId 対象の組織ノードID
     * @param parentAncestors 親ノードの祖先関係一覧
     * @return 組織階層関係一覧。該当しない場合は空リスト
     */
    public List<OrgNodeClosure> buildClosuresForNewNode(Long nodeId, List<OrgNodeClosure> parentAncestors) {
        List<OrgNodeClosure> closures = new ArrayList<>();
        closures.add(new OrgNodeClosure(nodeId, nodeId, 0));

        if (parentAncestors == null || parentAncestors.isEmpty()) {
            return closures;
        }

        for (OrgNodeClosure ancestor : parentAncestors) {
            closures.add(new OrgNodeClosure(ancestor.getAncestorId(), nodeId, ancestor.getDepth() + 1));
        }
        return closures;
    }

    /**
     * 指定組織ノードに子孫ノードが存在するか判定する。
     *
     * @param descendants 対象ノードの子孫関係一覧
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    public boolean hasDescendants(List<OrgNodeClosure> descendants) {
        if (descendants == null || descendants.isEmpty()) {
            return false;
        }
        return descendants.stream()
                .anyMatch(closure -> closure.getDepth() != null && closure.getDepth() > 0);
    }

    /**
     * 指定した組織ノード一覧に対象ノードが含まれるか判定する。
     *
     * @param subtree 移動対象サブツリーの階層関係一覧
     * @param nodeId 対象の組織ノードID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    public boolean containsNode(List<OrgNodeClosure> subtree, Long nodeId) {
        if (subtree == null || nodeId == null) {
            return false;
        }
        return subtree.stream()
                .anyMatch(closure -> nodeId.equals(closure.getDescendantId()));
    }

    /**
     * 移動後のサブツリーに対する外部階層関係を構築する。
     *
     * @param newParentAncestors 移動先親ノードの祖先関係一覧
     * @param subtreeDescendants 移動対象サブツリー内の子孫関係一覧
     * @return 組織階層関係一覧。該当しない場合は空リスト
     */
    public List<OrgNodeClosure> buildExternalClosuresForMovedSubtree(
            List<OrgNodeClosure> newParentAncestors,
            List<OrgNodeClosure> subtreeDescendants) {
        List<OrgNodeClosure> closures = new ArrayList<>();
        if (newParentAncestors == null || subtreeDescendants == null) {
            return closures;
        }
        for (OrgNodeClosure parentAncestor : newParentAncestors) {
            for (OrgNodeClosure descendant : subtreeDescendants) {
                closures.add(new OrgNodeClosure(
                        parentAncestor.getAncestorId(),
                        descendant.getDescendantId(),
                        parentAncestor.getDepth() + 1 + descendant.getDepth()));
            }
        }
        return closures;
    }
}
