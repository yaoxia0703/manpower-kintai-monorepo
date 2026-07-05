package com.manpowergroup.kintai.framework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConversionConfig implements WebMvcConfigurer {

    private final StatusConverter statusConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(statusConverter);
    }
}
