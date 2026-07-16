package com.manpowergroup.kintai.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Getter
public enum PermissionHttpMethod {

    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    CONNECT("CONNECT");

    @EnumValue
    private final String code;

    PermissionHttpMethod(String code) {
        this.code = code;
    }

    @JsonValue
    public String toJson() {
        return code;
    }

    @JsonCreator
    public static PermissionHttpMethod fromJson(String code) {
        if (code == null) {
            return null;
        }
        return fromCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid permission HTTP method: " + code));
    }

    public static Optional<PermissionHttpMethod> fromCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        String normalized = code.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(value -> value.code.equals(normalized))
                .findFirst();
    }
}
