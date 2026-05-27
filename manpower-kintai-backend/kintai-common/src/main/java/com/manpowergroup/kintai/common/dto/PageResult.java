package com.manpowergroup.kintai.common.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// ページングレスポンス共通DTO
@Data
public class PageResult<T> {

    private List<T> records;
    private long total;
    private long page;
    private long size;
    private long pages;

    public static <T> PageResult<T> of(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setPage(page.getCurrent());
        result.setSize(page.getSize());
        result.setPages(page.getPages());
        return result;
    }

    // Entity → VO変換用
    public static <T, R> PageResult<R> of(Page<T> page, Function<T, R> converter) {
        PageResult<R> result = new PageResult<>();
        result.setRecords(page.getRecords().stream()
                .map(converter)
                .collect(Collectors.toList()));
        result.setTotal(page.getTotal());
        result.setPage(page.getCurrent());
        result.setSize(page.getSize());
        result.setPages(page.getPages());
        return result;
    }
}