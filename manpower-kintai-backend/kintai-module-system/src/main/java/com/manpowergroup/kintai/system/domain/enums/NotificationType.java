package com.manpowergroup.kintai.system.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

// 通知タイプ（システム内部で発火するイベント種別）
@Getter
public enum NotificationType {

    REQUEST_SUBMITTED("REQUEST_SUBMITTED"),
    REQUEST_APPROVED("REQUEST_APPROVED"),
    REQUEST_REJECTED("REQUEST_REJECTED"),
    REQUEST_CANCELLED("REQUEST_CANCELLED");

    @EnumValue
    private final String code;

    NotificationType(String code) {
        this.code = code;
    }

    @JsonValue
    public String toJson() {
        return code;
    }

    @JsonCreator
    public static NotificationType fromJson(String code) {
        return Arrays.stream(values())
                .filter(v -> v.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid notification type: " + code));
    }
}