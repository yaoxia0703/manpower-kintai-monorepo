package com.manpowergroup.kintai.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

// 出勤区分
@Getter
public enum AttendanceType {

    OFFICE("OFFICE", "出社"),
    REMOTE("REMOTE", "在宅勤務"),
    BUSINESS_TRIP("BUSINESS_TRIP", "出張"),
    HOLIDAY_WORK("HOLIDAY_WORK", "休日出勤");

    @EnumValue
    private final String code;
    private final String label;

    AttendanceType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    @JsonValue
    public String toJson() {
        return code;
    }

    @JsonCreator
    public static AttendanceType fromJson(String code) {
        return Arrays.stream(values())
                .filter(v -> v.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無効な出勤区分: " + code));
    }
}