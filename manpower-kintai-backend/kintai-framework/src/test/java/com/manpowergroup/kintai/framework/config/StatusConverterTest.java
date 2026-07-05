package com.manpowergroup.kintai.framework.config;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatusConverterTest {

    private final StatusConverter converter = new StatusConverter();

    @Test
    void convertsNumericQueryValueToStatus() {
        assertEquals(Status.ENABLED, converter.convert("1"));
        assertEquals(Status.DISABLED, converter.convert("0"));
    }

    @Test
    void keepsEnumNameQueryValueCompatible() {
        assertEquals(Status.ENABLED, converter.convert("ENABLED"));
        assertEquals(Status.DISABLED, converter.convert("disabled"));
    }
}
