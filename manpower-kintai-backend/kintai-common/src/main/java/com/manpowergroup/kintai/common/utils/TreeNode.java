package com.manpowergroup.kintai.common.utils;

import java.util.List;

public interface TreeNode<T> {
    Long getId();
    Long getParentId();
    Integer getSort();
    List<T> getChildren();
    void setChildren(List<T> children);
}