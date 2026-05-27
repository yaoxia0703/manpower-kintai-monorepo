package com.manpowergroup.kintai.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

// ページングリクエスト共通DTO
public record PageRequest(
        @Min(1) int page,
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