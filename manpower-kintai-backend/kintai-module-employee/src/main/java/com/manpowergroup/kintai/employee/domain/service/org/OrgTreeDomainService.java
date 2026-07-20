package com.manpowergroup.kintai.employee.domain.service.org;

import com.manpowergroup.kintai.employee.domain.entity.org.OrgNodeClosure;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/** 組織 Closure Table の構築とサブツリー移動計算を担うドメインサービス。 */
public class OrgTreeDomainService {

    /** 新規ノード自身と親の祖先を含む Closure 行を構築する。 */
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

    /** 自己行以外の子孫が存在するか判定する。 */
    public boolean hasDescendants(List<OrgNodeClosure> descendants) {
        if (descendants == null || descendants.isEmpty()) {
            return false;
        }
        return descendants.stream()
                .anyMatch(closure -> closure.getDepth() != null && closure.getDepth() > 0);
    }

    /** サブツリーに指定ノードが含まれるか判定する。 */
    public boolean containsNode(List<OrgNodeClosure> subtree, Long nodeId) {
        if (subtree == null || nodeId == null) {
            return false;
        }
        return subtree.stream()
                .anyMatch(closure -> nodeId.equals(closure.getDescendantId()));
    }

    /** サブツリー移動後に必要な外部祖先との Closure 行を再構築する。 */
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
