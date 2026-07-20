package com.manpowergroup.kintai.employee.domain.service.org;

import com.manpowergroup.kintai.employee.domain.entity.org.OrgNodeClosure;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrgTreeDomainServiceTest {

    private final OrgTreeDomainService service = new OrgTreeDomainService();

    @Test
    void buildClosuresForRootNodeAddsOnlySelfClosure() {
        List<OrgNodeClosure> closures = service.buildClosuresForNewNode(100L, List.of());

        assertEquals(1, closures.size());
        assertEquals(100L, closures.get(0).getAncestorId());
        assertEquals(100L, closures.get(0).getDescendantId());
        assertEquals(0, closures.get(0).getDepth());
    }

    @Test
    void buildClosuresForChildNodeInheritsParentAncestors() {
        List<OrgNodeClosure> closures = service.buildClosuresForNewNode(200L, List.of(
                new OrgNodeClosure(10L, 10L, 0),
                new OrgNodeClosure(1L, 10L, 1)
        ));

        assertEquals(3, closures.size());
        assertEquals(200L, closures.get(0).getAncestorId());
        assertEquals(200L, closures.get(0).getDescendantId());
        assertEquals(0, closures.get(0).getDepth());
        assertEquals(10L, closures.get(1).getAncestorId());
        assertEquals(200L, closures.get(1).getDescendantId());
        assertEquals(1, closures.get(1).getDepth());
        assertEquals(1L, closures.get(2).getAncestorId());
        assertEquals(200L, closures.get(2).getDescendantId());
        assertEquals(2, closures.get(2).getDepth());
    }

    @Test
    void hasDescendantsIgnoresSelfClosure() {
        assertFalse(service.hasDescendants(List.of(new OrgNodeClosure(10L, 10L, 0))));
        assertTrue(service.hasDescendants(List.of(
                new OrgNodeClosure(10L, 10L, 0),
                new OrgNodeClosure(10L, 20L, 1)
        )));
    }

    @Test
    void detectsCandidateParentInsideSubtree() {
        List<OrgNodeClosure> subtree = List.of(
                new OrgNodeClosure(10L, 10L, 0),
                new OrgNodeClosure(10L, 20L, 1),
                new OrgNodeClosure(10L, 30L, 2));

        assertTrue(service.containsNode(subtree, 20L));
        assertFalse(service.containsNode(subtree, 40L));
    }

    @Test
    void buildsExternalClosuresWhenMovingSubtree() {
        List<OrgNodeClosure> newParentAncestors = List.of(
                new OrgNodeClosure(50L, 50L, 0),
                new OrgNodeClosure(1L, 50L, 1));
        List<OrgNodeClosure> subtree = List.of(
                new OrgNodeClosure(10L, 10L, 0),
                new OrgNodeClosure(10L, 20L, 1));

        List<OrgNodeClosure> closures = service.buildExternalClosuresForMovedSubtree(
                newParentAncestors, subtree);

        assertEquals(List.of(
                new OrgNodeClosure(50L, 10L, 1),
                new OrgNodeClosure(50L, 20L, 2),
                new OrgNodeClosure(1L, 10L, 2),
                new OrgNodeClosure(1L, 20L, 3)
        ), closures);
    }
}
