package com.manpowergroup.kintai.attendance.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ApprovalStopCondition {

    DIRECT_ONLY("DIRECT_ONLY"),
    REACH_GRADE("REACH_GRADE"),
    REACH_DEPARTMENT("REACH_DEPARTMENT");

    @EnumValue
    private final String code;

    ApprovalStopCondition(String code) {
        this.code = code;
    }

    @JsonValue
    public String toJson() {
        return code;
    }

    @JsonCreator
    public static ApprovalStopCondition fromJson(String code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid approval stop condition: " + code));
    }
}
