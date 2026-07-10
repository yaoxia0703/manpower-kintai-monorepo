package com.manpowergroup.kintai.common.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// ページングレスポンス共通DTO
@Data
@Schema(description = "ページングレスポンス")
public class PageResult<T> {

    @Schema(description = "データリスト")
    private List<T> records;

    @Schema(description = "総件数", example = "100")
    private long total;

    @Schema(description = "現在のページ番号", example = "1")
    private long page;

    @Schema(description = "1ページあたりの件数", example = "10")
    private long size;

    @Schema(description = "総ページ数", example = "10")
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

    public <R> PageResult<R> map(Function<T, R> converter) {
        PageResult<R> result = new PageResult<>();
        result.setRecords(records == null
                ? List.of()
                : records.stream().map(converter).collect(Collectors.toList()));
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        result.setPages(pages);
        return result;
    }
}
