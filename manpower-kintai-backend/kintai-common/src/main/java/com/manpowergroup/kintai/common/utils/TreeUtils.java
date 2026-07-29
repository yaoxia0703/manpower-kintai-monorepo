package com.manpowergroup.kintai.common.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TreeUtils {
    private TreeUtils() {}


    public static <T extends TreeNode<T>> List<T> buildTree(List<T> all, Long parentId) {
        return all.stream()
                .filter(node -> Objects.equals(node.getParentId(), parentId))
                .peek(node -> node.setChildren(buildTree(all, node.getId())))
                .sorted(Comparator.comparing(TreeNode::getSort))
                .toList();
    }
}
