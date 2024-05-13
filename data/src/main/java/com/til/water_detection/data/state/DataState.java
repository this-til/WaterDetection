package com.til.water_detection.data.state;

import lombok.Getter;


@Getter
public enum DataState {


    EXCEPTION_UPPER(0x01),
    WARN_UPPER(0x02),
    NORMAL(0x03),
    WARN_LOWER(0x04),
    EXCEPTION_LOWER(0x05);


    private final int state;

    DataState(int state) {
        this.state = state;
    }
}
