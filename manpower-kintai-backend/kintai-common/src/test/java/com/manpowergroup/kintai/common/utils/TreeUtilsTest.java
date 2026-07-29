package com.manpowergroup.kintai.common.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeUtilsTest {

    @Test
    void buildsTreeWhenRootParentIdIsNull() {
        TestNode rootWithHigherSort = new TestNode(1L, null, 20);
        TestNode child = new TestNode(2L, 1L, 10);
        TestNode rootWithLowerSort = new TestNode(3L, null, 10);

        List<TestNode> roots = TreeUtils.buildTree(
            List.of(rootWithHigherSort, child, rootWithLowerSort), null);

        assertEquals(List.of(rootWithLowerSort, rootWithHigherSort), roots);
        assertEquals(List.of(child), rootWithHigherSort.getChildren());
    }

    private static final class TestNode implements TreeNode<TestNode> {
        private final Long id;
        private final Long parentId;
        private final Integer sort;
        private List<TestNode> children = List.of();

        private TestNode(Long id, Long parentId, Integer sort) {
            this.id = id;
            this.parentId = parentId;
            this.sort = sort;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public Long getParentId() {
            return parentId;
        }

        @Override
        public Integer getSort() {
            return sort;
        }

        @Override
        public List<TestNode> getChildren() {
            return children;
        }

        @Override
        public void setChildren(List<TestNode> children) {
            this.children = children;
        }
    }
}
