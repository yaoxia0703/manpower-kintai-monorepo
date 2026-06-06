package com.manpowergroup.kintai.system.application.service.impl.org;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.application.command.org.NodeCreateCommand;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNode;
import com.manpowergroup.kintai.system.domain.entity.org.OrgNodeClosure;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeClosureRepository;
import com.manpowergroup.kintai.system.domain.repository.org.OrgNodeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class OrgNodeServiceImplTest {

    @Test
    void createSavesSelfClosureForRootNode() {
        OrgNodeRepository nodeRepository = mock(OrgNodeRepository.class);
        OrgNodeClosureRepository closureRepository = mock(OrgNodeClosureRepository.class);
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository);
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
        OrgNodeServiceImpl service = new OrgNodeServiceImpl(nodeRepository, closureRepository);
        when(nodeRepository.save(any(OrgNode.class))).thenAnswer(invocation -> {
            OrgNode node = invocation.getArgument(0);
            node.setId(200L);
            return node;
        });
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
}
