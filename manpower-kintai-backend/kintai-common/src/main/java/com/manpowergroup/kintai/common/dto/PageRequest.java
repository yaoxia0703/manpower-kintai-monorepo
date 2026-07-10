package com.manpowergroup.kintai.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

// ページングリクエスト共通DTO
@Schema(description = "ページングリクエスト")
public record PageRequest(
        @Schema(description = "ページ番号", example = "1")
        @Min(1) int page,
        @Schema(description = "1ページあたりの件数", example = "10")
        @Min(1) @Max(100) int size
) {
    public PageRequest {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }
}
