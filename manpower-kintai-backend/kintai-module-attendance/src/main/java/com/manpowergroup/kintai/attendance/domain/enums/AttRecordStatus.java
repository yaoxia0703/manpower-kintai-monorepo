package com.manpowergroup.kintai.attendance.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AttRecordStatus {

    DRAFT(0),
    SUBMITTED(1),
    LOCKED(2);

    @EnumValue
    private final int code;

    AttRecordStatus(int code) {
        this.code = code;
    }

    @JsonValue
    public int toJson() {
        return code;
    }

    @JsonCreator
    public static AttRecordStatus fromJson(int code) {
        return Arrays.stream(values())
                .filter(v -> v.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendance record status: " + code));
    }
}
