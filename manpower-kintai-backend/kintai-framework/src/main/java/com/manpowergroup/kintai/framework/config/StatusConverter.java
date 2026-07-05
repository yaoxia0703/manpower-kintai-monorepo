package com.manpowergroup.kintai.framework.config;

import com.manpowergroup.kintai.common.enums.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
public class StatusConverter implements Converter<String, Status> {

    @Override
    public Status convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }

        String value = source.trim();
        if (value.matches("-?\\d+")) {
            return Status.fromJson(Integer.parseInt(value));
        }

        String enumName = value.toUpperCase(Locale.ROOT);
        return Arrays.stream(Status.values())
                .filter(status -> status.name().equals(enumName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + source));
    }
}
