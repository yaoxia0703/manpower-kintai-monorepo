package com.manpowergroup.kintai.system.application.service.impl.org;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.system.application.command.org.NodeUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeClosureRepository;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeRepository;
import com.manpowergroup.kintai.system.domain.service.org.OrgTreeDomainService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class OrgNodeServiceImplTest {

    @Test
    void createSavesSelfClosureForRootNode() {
        OrgNodeRepository nodeRepository = mock(OrgNodeRepository.class);
        OrgNodeClosureRepository closureRepository = mock(OrgNodeClosureRepository.class);
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository, new OrgTreeDomainService());
        when(nodeRepository.save(any(OrgNode.class))).thenAnswer(invocation -> {
            OrgNode node = invocation.getArgument(0);
            node.setId(100L);
            return node;
        });

        service.create(new NodeCreateCommand(
                1L, null, 2L, "本社", "HQ", "ADMIN", "HQ", 1, 10, Status.ENABLED));

        ArgumentCaptor<List<OrgNodeClosure>> captor = ArgumentCaptor.forClass(List.class);
        verify(closureRepository).saveBatch(captor.capture());
        List<OrgNodeClosure> closures = captor.getValue();
        assertEquals(1, closures.size());
        assertEquals(100L, closures.get(0).getAncestorId());
        assertEquals(100L, closures.get(0).getDescendantId());
        assertEquals(0, closures.get(0).getDepth());
    }

    @Test
    void createInheritsParentAncestorsAndAddsSelfClosure() {
        OrgNodeRepository nodeRepository = mock(OrgNodeRepository.class);
        OrgNodeClosureRepository closureRepository = mock(OrgNodeClosureRepository.class);
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository, new OrgTreeDomainService());
        when(nodeRepository.save(any(OrgNode.class))).thenAnswer(invocation -> {
            OrgNode node = invocation.getArgument(0);
            node.setId(200L);
            return node;
        });
        when(nodeRepository.findById(10L)).thenReturn(Optional.of(
                node(10L, 1L, null, 1)));
        when(closureRepository.findAncestors(10L)).thenReturn(List.of(
                new OrgNodeClosure(10L, 10L, 0),
                new OrgNodeClosure(1L, 10L, 1)
        ));

        service.create(new NodeCreateCommand(
                1L, 10L, 2L, "営業部", "DEPT", "SALES", "SALES", 2, 20, Status.ENABLED));

        ArgumentCaptor<List<OrgNodeClosure>> captor = ArgumentCaptor.forClass(List.class);
        verify(closureRepository).saveBatch(captor.capture());
        List<OrgNodeClosure> closures = captor.getValue();
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
    void createRejectsParentFromAnotherCompany() {
        OrgNodeRepository nodeRepository = mock(OrgNodeRepository.class);
        OrgNodeClosureRepository closureRepository = mock(OrgNodeClosureRepository.class);
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository, new OrgTreeDomainService());
        when(nodeRepository.findById(10L)).thenReturn(Optional.of(
                node(10L, 2L, null, 1)));

        assertThrows(BizException.class, () -> service.create(new NodeCreateCommand(
                1L, 10L, 2L, "Sales", "DEPT", "SALES", "SALES", 99, 20, Status.ENABLED)));

        verify(nodeRepository, never()).save(any(OrgNode.class));
    }

    @Test
    void createDerivesLevelFromParent() {
        OrgNodeRepository nodeRepository = mock(OrgNodeRepository.class);
        OrgNodeClosureRepository closureRepository = mock(OrgNodeClosureRepository.class);
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository, new OrgTreeDomainService());
        when(nodeRepository.findById(10L)).thenReturn(Optional.of(
                node(10L, 1L, null, 2)));
        when(nodeRepository.save(any(OrgNode.class))).thenAnswer(invocation -> {
            OrgNode node = invocation.getArgument(0);
            node.setId(200L);
            return node;
        });
        when(closureRepository.findAncestors(10L)).thenReturn(List.of(
                new OrgNodeClosure(10L, 10L, 0)));

        service.create(new NodeCreateCommand(
                1L, 10L, 2L, "Sales", "DEPT", "SALES", "SALES", 99, 20, Status.ENABLED));

        ArgumentCaptor<OrgNode> nodeCaptor = ArgumentCaptor.forClass(OrgNode.class);
        verify(nodeRepository).save(nodeCaptor.capture());
        assertEquals(3, nodeCaptor.getValue().getLevel());
    }

    @Test
    void updateRejectsMovingNodeBelowOwnDescendant() {
        OrgNodeRepository nodeRepository = mock(OrgNodeRepository.class);
        OrgNodeClosureRepository closureRepository = mock(OrgNodeClosureRepository.class);
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository, new OrgTreeDomainService());
        OrgNode node = node(10L, 1L, null, 1);
        when(nodeRepository.findById(10L)).thenReturn(Optional.of(node));
        when(closureRepository.findDescendants(10L)).thenReturn(List.of(
                new OrgNodeClosure(10L, 10L, 0),
                new OrgNodeClosure(10L, 20L, 1)));

        NodeUpdateCommand command = new NodeUpdateCommand(
                1L, 20L, 2L, "Sales", "DEPT", "SALES", "SALES", 2, 20, Status.ENABLED);

        assertThrows(BizException.class, () -> service.update(10L, command));
        verify(nodeRepository, never()).update(any(OrgNode.class));
    }

    @Test
    void updateMovesSubtreeAndRebuildsExternalClosures() {
        OrgNodeRepository nodeRepository = mock(OrgNodeRepository.class);
        OrgNodeClosureRepository closureRepository = mock(OrgNodeClosureRepository.class);
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository, new OrgTreeDomainService());
        OrgNode node = node(10L, 1L, 5L, 2);
        OrgNode child = node(20L, 1L, 10L, 3);
        OrgNode newParent = node(50L, 1L, null, 4);
        when(nodeRepository.findById(10L)).thenReturn(Optional.of(node));
        when(nodeRepository.findById(20L)).thenReturn(Optional.of(child));
        when(nodeRepository.findById(50L)).thenReturn(Optional.of(newParent));
        List<OrgNodeClosure> subtree = List.of(
                new OrgNodeClosure(10L, 10L, 0),
                new OrgNodeClosure(10L, 20L, 1));
        when(closureRepository.findDescendants(10L)).thenReturn(subtree);
        when(closureRepository.findAncestors(50L)).thenReturn(List.of(
                new OrgNodeClosure(50L, 50L, 0),
                new OrgNodeClosure(1L, 50L, 1)));

        service.update(10L, new NodeUpdateCommand(
                1L, 50L, 2L, "Sales", "DEPT", "SALES", "SALES", 99, 20, Status.ENABLED));

        assertEquals(50L, node.getParentId());
        assertEquals(5, node.getLevel());
        assertEquals(6, child.getLevel());
        verify(closureRepository).deleteExternalAncestorLinks(List.of(10L, 20L));
        ArgumentCaptor<List<OrgNodeClosure>> closureCaptor = ArgumentCaptor.forClass(List.class);
        verify(closureRepository).saveBatch(closureCaptor.capture());
        assertEquals(List.of(
                new OrgNodeClosure(50L, 10L, 1),
                new OrgNodeClosure(50L, 20L, 2),
                new OrgNodeClosure(1L, 10L, 2),
                new OrgNodeClosure(1L, 20L, 3)
        ), closureCaptor.getValue());
        verify(nodeRepository).update(node);
        verify(nodeRepository).update(child);
    }

    @Test
    void removeRejectsNodeWithDescendants() {
        OrgNodeRepository nodeRepository = mock(OrgNodeRepository.class);
        OrgNodeClosureRepository closureRepository = mock(OrgNodeClosureRepository.class);
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository, new OrgTreeDomainService());
        when(nodeRepository.findById(10L)).thenReturn(Optional.of(node(10L, 1L, null, 1)));
        when(closureRepository.findDescendants(10L)).thenReturn(List.of(
                new OrgNodeClosure(10L, 10L, 0),
                new OrgNodeClosure(10L, 20L, 1)
        ));

        assertThrows(BizException.class, () -> service.remove(10L));

        verify(closureRepository, never()).deleteByDescendantId(10L);
        verify(nodeRepository, never()).deleteById(10L);
    }

    private OrgNode node(Long id, Long companyId, Long parentId, int level) {
        OrgNode parent = null;
        for (int currentLevel = 1; currentLevel < level; currentLevel++) {
            Long currentId = currentLevel == level - 1
                    ? parentId
                    : Long.valueOf(-1L * currentLevel);
            parent = OrgNode.create(
                    companyId, parent, null, "Parent", "NODE", null,
                    "P" + currentLevel, currentLevel, Status.ENABLED).setId(currentId);
        }
        return OrgNode.create(
                companyId, parent, null, "Node", "NODE", null,
                "N" + id, 1, Status.ENABLED).setId(id);
    }
}
