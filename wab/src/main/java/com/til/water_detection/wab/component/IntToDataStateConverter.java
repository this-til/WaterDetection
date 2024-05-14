package com.til.water_detection.wab.component;

import com.til.water_detection.data.state.DataState;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class IntToDataStateConverter implements Converter<Integer, DataState> {

    @Override
    @Nullable
    public DataState convert(@NotNull Integer source) {

        DataState dataState = null;

        for (DataState value : DataState.values()) {
            if (value.getState() == source) {
                dataState = value;
                break;
            }
        }

        if (dataState == null) {
            throw new IllegalArgumentException("Cannot convert String to DataState: " + source);
        }

        return dataState;

    }
}