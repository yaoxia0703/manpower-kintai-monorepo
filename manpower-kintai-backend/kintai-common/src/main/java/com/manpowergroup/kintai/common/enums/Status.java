package com.manpowergroup.kintai.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

// 有効/無効ステータス（共通）
@Getter
public enum Status {

    DISABLED((byte) 0, "無効"),
    ENABLED((byte) 1, "有効");

    @EnumValue
    private final byte code;
    private final String label;

    Status(byte code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonValue
    public int toJson() {
        return code;
    }

    @JsonCreator
    public static Status fromJson(int code) {
        final byte b = (byte) code;
        return Arrays.stream(values())
                .filter(v -> v.code == b)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無効な状態: " + code));
    }
}