package com.manpowergroup.kintai.attendance.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MonthlySummaryStatus {

    OPEN(0),
    CONFIRMED(1);

    @EnumValue
    private final int code;

    MonthlySummaryStatus(int code) {
        this.code = code;
    }

    @JsonValue
    public int toJson() {
        return code;
    }

    @JsonCreator
    public static MonthlySummaryStatus fromJson(int code) {
        return Arrays.stream(values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid monthly summary status: " + code));
    }
}
