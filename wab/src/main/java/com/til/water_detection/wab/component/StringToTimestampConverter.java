package com.til.water_detection.wab.component;

import io.micrometer.common.util.StringUtils;
import org.apache.catalina.util.StringUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class StringToTimestampConverter implements Converter<String, Timestamp> {

    @Override
    @Nullable
    public Timestamp convert(String source) {
        try {
            return new Timestamp(Long.parseLong(source));
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert String to Timestamp: " + source);
        }
    }
}