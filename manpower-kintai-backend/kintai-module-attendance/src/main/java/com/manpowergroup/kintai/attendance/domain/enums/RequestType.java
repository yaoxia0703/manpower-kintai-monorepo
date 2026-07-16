package com.manpowergroup.kintai.attendance.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RequestType {

    PAID_LEAVE("PAID_LEAVE", true, true),
    OVERTIME("OVERTIME", true, false),
    SUBSTITUTE("SUBSTITUTE", true, true),
    BUSINESS_TRIP("BUSINESS_TRIP", false, false),
    LEAVE_OF_ABSENCE("LEAVE_OF_ABSENCE", false, true);

    @EnumValue
    private final String code;
    private final boolean creatable;
    private final boolean timesheetLocking;

    RequestType(String code, boolean creatable, boolean timesheetLocking) {
        this.code = code;
        this.creatable = creatable;
        this.timesheetLocking = timesheetLocking;
    }

    @JsonValue
    public String toJson() {
        return code;
    }

    @JsonCreator
    public static RequestType fromJson(String code) {
        return Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid request type: " + code));
    }
}
