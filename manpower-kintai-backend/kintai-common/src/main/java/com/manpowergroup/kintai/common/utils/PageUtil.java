package com.manpowergroup.kintai.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageRequest;

// MyBatis-Plus ページング変換ユーティリティ
public final class PageUtil {

    private PageUtil() {}

    public static <T> Page<T> toPage(PageRequest request) {
        if (request == null) {
            return new Page<>(1, 10);
        }
        return new Page<>(request.page(), request.size());
    }
}