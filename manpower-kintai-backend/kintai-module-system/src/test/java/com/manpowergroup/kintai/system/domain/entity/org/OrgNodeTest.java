package com.manpowergroup.kintai.system.domain.entity.org;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrgNodeTest {

    @Test
    void createChildDerivesParentAndLevel() {
        OrgNode parent = node(10L, 1L, null, 2);

        OrgNode child = OrgNode.create(
                1L, parent, 20L, "Sales", "DEPT", "SALES", "SALES", 10, Status.ENABLED);

        assertEquals(10L, child.getParentId());
        assertEquals(3, child.getLevel());
    }

    @Test
    void createChildRejectsParentFromAnotherCompany() {
        OrgNode parent = node(10L, 2L, null, 1);

        assertThrows(BizException.class, () -> OrgNode.create(
                1L, parent, 20L, "Sales", "DEPT", "SALES", "SALES", 10, Status.ENABLED));
    }

    @Test
    void moveToParentDerivesNewLevel() {
        OrgNode node = node(20L, 1L, null, 2);
        OrgNode parent = node(30L, 1L, null, 4);

        node.moveTo(parent);

        assertEquals(30L, node.getParentId());
        assertEquals(5, node.getLevel());
    }

    @Test
    void updateEditableFieldsKeepsTreeIdentity() {
        OrgNode node = node(20L, 1L, 10L, 2);

        node.updateEditableFields(30L, "New Name", "TEAM", "SALES", "NEW", 40);

        assertEquals(30L, node.getManagerId());
        assertEquals("New Name", node.getName());
        assertEquals("TEAM", node.getTypeCode());
        assertEquals("SALES", node.getDeptFunction());
        assertEquals("NEW", node.getCode());
        assertEquals(40, node.getSort());
        assertEquals(1L, node.getCompanyId());
        assertEquals(10L, node.getParentId());
        assertEquals(2, node.getLevel());
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
