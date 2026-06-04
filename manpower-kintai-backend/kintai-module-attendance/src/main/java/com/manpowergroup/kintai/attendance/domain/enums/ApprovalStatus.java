package com.manpowergroup.kintai.attendance.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ApprovalStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    @EnumValue
    private final String code;

    ApprovalStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String toJson() {
        return code;
    }

    @JsonCreator
    public static ApprovalStatus fromJson(String code) {
        return Arrays.stream(values())
                .filter(v -> v.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid approval status: " + code));
    }
}
